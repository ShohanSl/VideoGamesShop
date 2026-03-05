package com.example.videogamesshop.dto.developer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.Data;

@Data
public class DeveloperCreateRequest {
    @NotBlank(message = "Name is required")
    private String name;
    private String country;
    @Past(message = "Founded date must be in the past")
    private LocalDate foundedDate;
}