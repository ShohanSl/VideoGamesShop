package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.videogamesshop.dto.auth.AuthResponse;
import com.example.videogamesshop.dto.auth.LoginRequest;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @Captor
    private ArgumentCaptor<JwtPrincipal> principalCaptor;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldAuthenticateAdmin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("ChangeMeAdmin123!");
        AppUserDetails userDetails = new AppUserDetails(null, "admin", "encoded", "ADMIN");

        when(authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated("admin", "ChangeMeAdmin123!")
        )).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(principalCaptor.capture())).thenReturn("jwt-token");

        AuthResponse response = authService.authenticateAdmin(request);

        assertEquals("jwt-token", response.getToken());
        assertEquals("ADMIN", response.getRole());
        assertEquals("admin", principalCaptor.getValue().username());
    }

    @Test
    void shouldAuthenticateUser() {
        LoginRequest request = new LoginRequest();
        request.setUsername("player_one");
        request.setPassword("StrongPass123!");
        AppUserDetails userDetails = new AppUserDetails(5L, "player_one", "encoded", "USER");

        when(authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated("player_one", "StrongPass123!")
        )).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(principalCaptor.capture())).thenReturn("jwt-token");

        AuthResponse response = authService.authenticateUser(request);

        assertEquals("jwt-token", response.getToken());
        assertEquals("USER", response.getRole());
        assertEquals(5L, response.getUserId());
        assertEquals("player_one", principalCaptor.getValue().username());
    }

    @Test
    void shouldRejectAdminLoginForUserAccount() {
        LoginRequest request = new LoginRequest();
        request.setUsername("player_one");
        request.setPassword("StrongPass123!");
        AppUserDetails userDetails = new AppUserDetails(5L, "player_one", "encoded", "USER");

        when(authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated("player_one", "StrongPass123!")
        )).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        assertThrows(BadCredentialsException.class, () -> authService.authenticateAdmin(request));
    }
}
