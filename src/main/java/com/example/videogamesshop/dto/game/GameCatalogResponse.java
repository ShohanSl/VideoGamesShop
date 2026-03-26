package com.example.videogamesshop.dto.game;

import com.example.videogamesshop.dto.category.CategoryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Game item for catalog responses")
public class GameCatalogResponse {
    private Long id;
    private String title;
    private Double price;
    private List<CategoryDto> categories;
}
