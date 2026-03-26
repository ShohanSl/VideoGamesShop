package com.example.videogamesshop.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Category response")
public class CategoryDto {
    private Long id;
    private String name;
}
