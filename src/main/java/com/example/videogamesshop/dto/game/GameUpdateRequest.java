package com.example.videogamesshop.dto.game;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;


@Data
public class GameUpdateRequest {
    private String title;
    private Double price;
    private LocalDate releaseDate;
    private String description;
    private List<Long> categoryIds;
}