package com.example.videogamesshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameFullResponse {
    private Long id;
    private String title;
    private String genre;
    private Double price;
    private LocalDate releaseDate;
    private String description;
}