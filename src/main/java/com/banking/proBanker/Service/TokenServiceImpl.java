package com.banking.proBanker.Service;

import com.banking.proBanker.Entity.Token;
import com.banking.proBanker.Exceptions.InvalidTokenException;
import com.banking.proBanker.Repository.AccountRepository;
import com.banking.proBanker.Repository.TokenRepository;
import com.banking.proBanker.Repository.UserRepository;
import com.banking.proBanker.Utilities.ApiMessages;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AccountRepository accountRepository;


    @Override
    public String generateToken(UserDetails userDetails) {
        log.info("Generation token for user: " + userDetails.getUsername());
        return doGenerateToken(userDetails,
                new Date(System.currentTimeMillis() + expiration));
    }

    @Override
    public String generateToken(UserDetails userDetails, Date expiry) {
        log.info("Generation token for user: " + userDetails.getUsername());
        return doGenerateToken(userDetails, expiry);
    }

    @Override
    public UserDetails loadUserByUsername(String accountNumber) throws UsernameNotFoundException {
        val user = userRepository.findByAccountAccountNumber(accountNumber)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(ApiMessages.USER_NOT_FOUND_BY_ACCOUNT.getMessage(), accountNumber)));

        return withUsername(accountNumber).password(user.getPassword()).build();
    }

    @Override
    public String getUsernameFromToken(String token) throws InvalidTokenException {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public Date getExpirationDateFromToken(String token) throws InvalidTokenException {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) throws InvalidTokenException {
        val claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    @Override
    public void saveToken(String token) throws InvalidTokenException {
        if (tokenRepository.findByToken(token) != null) {
            throw new InvalidTokenException(ApiMessages.TOKEN_ALREADY_EXISTS_ERROR.getMessage());
        }
        val account = accountRepository.findByAccountNumber(
                getUsernameFromToken(token)
        );

        log.info("Saving token for account: " + account.getAccountNumber());

        val tokenObj = new Token(
                token,
                getExpirationDateFromToken(token),
                account
        );
        tokenRepository.save(tokenObj);
    }

    @Override
    public void validateToken(String token) throws InvalidTokenException {
        if (tokenRepository.findByToken(token) == null) {
            throw new InvalidTokenException(ApiMessages.TOKEN_NOT_FOUND_ERROR.getMessage());
        }
    }

    @Override
    public void invalidateToken(String token) {
        if (tokenRepository.findByToken(token) != null) {
            tokenRepository.deleteByToken(token);
        }
    }

    //NOT PRESENT IN INTERFACE
    private String doGenerateToken (UserDetails userDetails, Date expiry) {
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private Claims getAllClaimsFromToken (String token) throws InvalidTokenException {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            // Delete expired token
            invalidateToken(token);

            throw new InvalidTokenException(ApiMessages.TOKEN_EXPIRED_ERROR.getMessage());

        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException(ApiMessages.TOKEN_UNSUPPORTED_ERROR.getMessage());

        } catch (MalformedJwtException e) {
            throw new InvalidTokenException(ApiMessages.TOKEN_MALFORMED_ERROR.getMessage());

        } catch (SignatureException e) {
            throw new InvalidTokenException(ApiMessages.TOKEN_SIGNATURE_INVALID_ERROR.getMessage());

        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException(ApiMessages.TOKEN_EMPTY_ERROR.getMessage());
        }
    }
}
