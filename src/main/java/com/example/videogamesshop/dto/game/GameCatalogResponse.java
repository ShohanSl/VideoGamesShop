package com.example.videogamesshop.dto.game;

import com.example.videogamesshop.dto.category.CategoryDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameCatalogResponse {
    private Long id;
    private String title;
    private Double price;
    private List<CategoryDto> categories;
}