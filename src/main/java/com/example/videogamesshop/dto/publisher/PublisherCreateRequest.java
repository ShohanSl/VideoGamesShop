package com.example.videogamesshop.dto.publisher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.Data;

@Data
public class PublisherCreateRequest {
    @NotBlank(message = "Publisher name is required")
    private String name;
    private String country;
    @Past(message = "Founded date must be in the past")
    private LocalDate foundedDate;
}