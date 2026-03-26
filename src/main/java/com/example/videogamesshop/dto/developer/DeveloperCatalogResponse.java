package com.example.videogamesshop.dto.developer;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "Developer item for catalog responses")
public class DeveloperCatalogResponse {
    private Long id;
    private String name;
    private String country;
    private LocalDate foundedDate;
}
