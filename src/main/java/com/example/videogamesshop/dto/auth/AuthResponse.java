package com.example.videogamesshop.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "JWT authentication response")
public class AuthResponse {
    private String token;
    private String role;
    private Long userId;
    private String username;
}
