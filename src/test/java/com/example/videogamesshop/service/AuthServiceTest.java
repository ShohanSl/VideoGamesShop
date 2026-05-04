package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.videogamesshop.dto.auth.AuthResponse;
import com.example.videogamesshop.dto.auth.LoginRequest;
import com.example.videogamesshop.entity.User;
import com.example.videogamesshop.entity.UserRole;
import com.example.videogamesshop.repository.UserRepository;
import com.example.videogamesshop.security.AppUserDetails;
import com.example.videogamesshop.security.JwtPrincipal;
import com.example.videogamesshop.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private RevokedTokenService revokedTokenService;

    @Captor
    private ArgumentCaptor<JwtPrincipal> principalCaptor;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldAuthenticateExistingUser() {
        LoginRequest request = new LoginRequest();
        request.setUsername("player_one");
        request.setPassword("StrongPass123!");
        AppUserDetails userDetails = new AppUserDetails(5L, "player_one", "encoded", "USER");

        when(authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated("player_one", "StrongPass123!")
        )).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(principalCaptor.capture())).thenReturn("jwt-token");

        AuthResponse response = authService.authenticate(request);

        assertEquals("jwt-token", response.getToken());
        assertEquals("USER", response.getRole());
        assertEquals(5L, response.getUserId());
        assertEquals("player_one", principalCaptor.getValue().username());
    }

    @Test
    void shouldBootstrapFirstAdmin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("ChangeMeAdmin123!");
        User savedAdmin = new User();
        savedAdmin.setId(11L);
        savedAdmin.setUsername("admin");
        savedAdmin.setRole(UserRole.ADMIN);
        savedAdmin.setPasswordHash("encoded-admin-password");

        when(userRepository.countByRole(UserRole.ADMIN)).thenReturn(0L);
        when(passwordEncoder.encode("ChangeMeAdmin123!")).thenReturn("encoded-admin-password");
        when(userRepository.save(any(User.class))).thenReturn(savedAdmin);
        when(jwtService.generateToken(principalCaptor.capture())).thenReturn("jwt-token");

        AuthResponse response = authService.authenticate(request);

        assertEquals("jwt-token", response.getToken());
        assertEquals("ADMIN", response.getRole());
        assertEquals(11L, response.getUserId());
        assertEquals("admin", response.getUsername());
        verify(passwordEncoder).encode("ChangeMeAdmin123!");
    }

    @Test
    void shouldRevokeTokenOnLogout() {
        authService.logout("Bearer jwt-token");

        verify(revokedTokenService).revoke("jwt-token");
    }
}
