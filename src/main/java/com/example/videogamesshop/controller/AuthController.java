package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.auth.AuthResponse;
import com.example.videogamesshop.dto.auth.LoginRequest;
import com.example.videogamesshop.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "JWT authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/admin")
    @Operation(summary = "Authenticate administrator by username and password")
    public AuthResponse loginAsAdmin(@Valid @RequestBody LoginRequest request) {
        return authService.authenticateAdmin(request);
    }

    @PostMapping("/user")
    @Operation(summary = "Authenticate user by username and password")
    public AuthResponse loginUser(@Valid @RequestBody LoginRequest request) {
        return authService.authenticateUser(request);
    }
}
