package com.example.videogamesshop.dto.developer;

import java.time.LocalDate;
import lombok.Data;

@Data
public class DeveloperCatalogResponse {
    private Long id;
    private String name;
    private String country;
    private LocalDate foundedDate;
}