package com.banking.proBanker.Service;

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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
    @Value("${jwt_secret}")
    private String secret;

    @Value("${jwt_expiration}")
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

    }

    @Override
    public void validateToken(String token) throws InvalidTokenException {

    }

    @Override
    public void invalidateToken(String token) {

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
