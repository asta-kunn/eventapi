package com.example.eventapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${app.jwtSecret}") private String jwtSecret;
    @Value("${app.jwtExpirationMs}") private int jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(org.springframework.security.core.Authentication auth) {
        var user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        return Jwts.builder()
                   .subject(user.getUsername())
                   .issuedAt(new Date())
                   .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                   .signWith(getSigningKey(), Jwts.SIG.HS512)
                   .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                   .verifyWith(getSigningKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            // log.warn("JWT invalid: {}", e.getMessage());
        }
        return false;
    }
}
