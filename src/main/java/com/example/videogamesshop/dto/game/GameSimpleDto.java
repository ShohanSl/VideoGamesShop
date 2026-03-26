package com.example.videogamesshop.dto.game;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Compact game response")
public class GameSimpleDto {
    private Long id;
    private String title;
}
