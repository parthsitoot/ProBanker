package com.banking.proBanker.Service;

import com.banking.proBanker.Exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
import java.util.function.Function;

public interface TokenService extends UserDetailsService {
    public String generateToken (UserDetails userDetails);

    public String generateToken (UserDetails userDetails, Date expiry);

    UserDetails loadUserByUsername(String accountNumber) throws UsernameNotFoundException;

    public String getUsernameFromToken (String token) throws InvalidTokenException;

    public Date getExpirationDateFromToken (String token) throws InvalidTokenException;

    public <T> T getClaimFromToken (String token, Function<Claims, T> claimResolver) throws InvalidTokenException;

    public void saveToken (String token) throws InvalidTokenException;

    public void validateToken (String token) throws  InvalidTokenException;

    public void invalidateToken (String token);


}
