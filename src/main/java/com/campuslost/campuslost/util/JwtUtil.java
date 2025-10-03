package com.campuslost.campuslost.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Simple JWT Utility - Only 3 methods!
 * 1. Generate token when user logs in
 * 2. Extract username from token
 * 3. Validate token (check if valid and not expired)
 */
@Component
public class JwtUtil {

    // Secret key for signing tokens
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // Token valid for 24 hours (in milliseconds)
    private final long JWT_EXPIRATION = 86400000;

    /**
     * 1. Generate JWT token for user
     * Called when user successfully logs in
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)                    // Store username in token
                .setIssuedAt(new Date())                 // When token was created
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) // When it expires
                .signWith(SECRET_KEY)                    // Sign with secret
                .compact();
    }

    /**
     * 2. Extract username from JWT token
     * Used to identify who the token belongs to
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 3. Validate JWT token
     * Check if token is valid and not expired
     */
    public boolean validateToken(String token, String username) {
        try {
            String tokenUsername = extractUsername(token);
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            
            return tokenUsername.equals(username) && expiration.after(new Date());
        } catch (Exception e) {
            return false;  // Token is invalid
        }
    }
}