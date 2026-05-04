package com.example.videogamesshop.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationSeconds;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-seconds}") long expirationSeconds) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(JwtPrincipal principal) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expirationSeconds);
        return Jwts.builder()
                .subject(principal.username())
                .claim("role", principal.role())
                .claim("userId", principal.userId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    public JwtPrincipal parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Number userIdClaim = claims.get("userId", Number.class);
        Long userId = userIdClaim != null ? userIdClaim.longValue() : null;
        return new JwtPrincipal(
                userId,
                claims.getSubject(),
                claims.get("role", String.class)
        );
    }
}
