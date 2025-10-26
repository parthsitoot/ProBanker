package com.banking.proBanker.Service;

import com.banking.proBanker.Exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface TokenService {
    public String generateToken (UserDetails userDetails);

    public String generateToken (UserDetails userDetails, Date expiry);

    public String getUsernameFromToken (String token) throws InvalidTokenException;

    public Date getExpirationDateFromToken (String token) throws InvalidTokenException;

    public <T> T getClaimFromToken (String token, Function<Claims, T> claimResolver) throws InvalidTokenException;

    public void saveToken (String token) throws InvalidTokenException;

    public void validateToken (String token) throws  InvalidTokenException;

    public void invalidateToken (String token);


}
