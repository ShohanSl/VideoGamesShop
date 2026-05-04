package com.example.videogamesshop.service;

import com.example.videogamesshop.dto.auth.AuthResponse;
import com.example.videogamesshop.dto.auth.LoginRequest;
import com.example.videogamesshop.entity.User;
import com.example.videogamesshop.entity.UserRole;
import com.example.videogamesshop.repository.UserRepository;
import com.example.videogamesshop.security.AppUserDetails;
import com.example.videogamesshop.security.JwtPrincipal;
import com.example.videogamesshop.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthService {

    private static final String FIRST_ADMIN_USERNAME = "admin";

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RevokedTokenService revokedTokenService;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RevokedTokenService revokedTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.revokedTokenService = revokedTokenService;
    }

    @Transactional
    public AuthResponse authenticate(LoginRequest request) {
        LoginRequest normalizedRequest = normalizeRequest(request);
        if (shouldBootstrapFirstAdmin(normalizedRequest)) {
            User admin = bootstrapFirstAdmin(normalizedRequest);
            return toAuthResponse(admin);
        }

        AppUserDetails userDetails = authenticateUserDetails(normalizedRequest);
        JwtPrincipal principal = new JwtPrincipal(
                userDetails.userId(),
                userDetails.getUsername(),
                userDetails.role()
        );
        return new AuthResponse(jwtService.generateToken(principal), principal.role(),
                userDetails.userId(), userDetails.getUsername());
    }

    public void logout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Invalid credentials");
        }
        revokedTokenService.revoke(authorizationHeader.substring(7));
    }

    private LoginRequest normalizeRequest(LoginRequest request) {
        LoginRequest normalizedRequest = new LoginRequest();
        normalizedRequest.setUsername(request.getUsername() == null ? null : request.getUsername().trim());
        normalizedRequest.setPassword(request.getPassword());
        return normalizedRequest;
    }

    private boolean shouldBootstrapFirstAdmin(LoginRequest request) {
        return FIRST_ADMIN_USERNAME.equals(request.getUsername())
                && userRepository.countByRole(UserRole.ADMIN) == 0;
    }

    private User bootstrapFirstAdmin(LoginRequest request) {
        User admin = new User();
        admin.setUsername(FIRST_ADMIN_USERNAME);
        admin.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        admin.setRole(UserRole.ADMIN);
        return userRepository.save(admin);
    }

    private AuthResponse toAuthResponse(User user) {
        JwtPrincipal principal = new JwtPrincipal(user.getId(), user.getUsername(), user.getRole().name());
        return new AuthResponse(
                jwtService.generateToken(principal),
                principal.role(),
                principal.userId(),
                principal.username()
        );
    }

    private AppUserDetails authenticateUserDetails(LoginRequest request) {
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
