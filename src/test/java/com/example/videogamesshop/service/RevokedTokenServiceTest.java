package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.videogamesshop.repository.RevokedTokenRepository;
import com.example.videogamesshop.security.JwtService;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RevokedTokenServiceTest {

    @Mock
    private RevokedTokenRepository revokedTokenRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RevokedTokenService revokedTokenService;

    @Test
    void shouldStoreRevokedToken() {
        when(jwtService.extractTokenId("jwt-token")).thenReturn("jti-1");
        when(jwtService.extractExpiration("jwt-token")).thenReturn(Instant.parse("2026-05-05T10:15:30Z"));

        revokedTokenService.revoke("jwt-token");

        verify(revokedTokenRepository).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldDetectRevokedToken() {
        when(jwtService.extractTokenId("jwt-token")).thenReturn("jti-2");
        when(revokedTokenRepository.existsById("jti-2")).thenReturn(true);

        boolean revoked = revokedTokenService.isRevoked("jwt-token");

        assertTrue(revoked);
    }
}
