package com.example.videogamesshop.service;

import com.example.videogamesshop.dto.auth.AuthResponse;
import com.example.videogamesshop.dto.auth.LoginRequest;
import com.example.videogamesshop.security.AppUserDetails;
import com.example.videogamesshop.security.JwtPrincipal;
import com.example.videogamesshop.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse authenticateAdmin(LoginRequest request) {
        AppUserDetails userDetails = authenticate(request);
        if (!"ADMIN".equals(userDetails.role())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        JwtPrincipal principal = new JwtPrincipal(null, userDetails.getUsername(), userDetails.role());
        return new AuthResponse(jwtService.generateToken(principal), principal.role(), null,
                principal.username());
    }

    public AuthResponse authenticateUser(LoginRequest request) {
        AppUserDetails userDetails = authenticate(request);
        if (!"USER".equals(userDetails.role())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        JwtPrincipal principal = new JwtPrincipal(
                userDetails.userId(),
                userDetails.getUsername(),
                userDetails.role()
        );
        return new AuthResponse(jwtService.generateToken(principal), principal.role(),
                userDetails.userId(), userDetails.getUsername());
    }

    private AppUserDetails authenticate(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            return (AppUserDetails) authentication.getPrincipal();
        } catch (BadCredentialsException ex) {
            log.warn("Authentication failed for username [{}]", request.getUsername());
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
