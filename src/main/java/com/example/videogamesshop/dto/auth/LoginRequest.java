package com.example.videogamesshop.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request body for username/password authentication")
public class LoginRequest {

    @Schema(description = "Username", example = "player_one")
    @NotBlank(message = "Username is required")
    @Size(max = 100, message = "Username must be at most 100 characters")
    private String username;

    @Schema(description = "Password", example = "StrongPass123!")
    @NotBlank(message = "Password is required")
    @Size(max = 200, message = "Password must be at most 200 characters")
    private String password;
}
