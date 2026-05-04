package com.example.videogamesshop.service;

import com.example.videogamesshop.entity.RevokedToken;
import com.example.videogamesshop.repository.RevokedTokenRepository;
import com.example.videogamesshop.security.JwtService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RevokedTokenService {

    private final RevokedTokenRepository revokedTokenRepository;
    private final JwtService jwtService;

    public void revoke(String token) {
        cleanupExpired();
        String jti = jwtService.extractTokenId(token);
        Instant expiresAt = jwtService.extractExpiration(token);
        revokedTokenRepository.save(new RevokedToken(jti, expiresAt));
    }

    @Transactional(readOnly = true)
    public boolean isRevoked(String token) {
        cleanupExpired();
        String jti = jwtService.extractTokenId(token);
        return revokedTokenRepository.existsById(jti);
    }

    public void cleanupExpired() {
        revokedTokenRepository.deleteByExpiresAtBefore(Instant.now());
    }
}
