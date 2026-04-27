package com.example.videogamesshop.dto.game;

import com.example.videogamesshop.dto.category.CategoryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
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
    private LocalDate releaseDate;
    private String description;
    private Long developerId;
    private String developerName;
    private Long publisherId;
    private String publisherName;
    private List<CategoryDto> categories;
}
