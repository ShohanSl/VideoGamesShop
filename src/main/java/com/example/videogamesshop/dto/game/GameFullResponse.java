package com.example.videogamesshop.dto.game;

import com.example.videogamesshop.dto.category.CategoryDto;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameFullResponse {
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