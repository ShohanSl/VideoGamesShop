package com.example.videogamesshop.dto.game;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;


@Data
public class GameRequest {
    private String title;
    private Double price;
    private LocalDate releaseDate;
    private String description;
    @NotNull(message = "Developer ID is required")
    private Long developerId;
    private Long publisherId;
    private List<Long> categoryIds;
}