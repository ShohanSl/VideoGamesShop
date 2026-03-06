package com.example.videogamesshop.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateRequest {
    @NotBlank(message = "Username is required")
    private String username;
}