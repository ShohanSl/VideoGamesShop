package com.example.videogamesshop.dto.library;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LibraryCreateRequest {
    @NotBlank(message = "Username is required")
    private String username;
}