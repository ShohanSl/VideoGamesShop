package com.example.videogamesshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameCatalogResponse {
    private Long id;
    private String title;
    private String genre;
    private Double price;
}