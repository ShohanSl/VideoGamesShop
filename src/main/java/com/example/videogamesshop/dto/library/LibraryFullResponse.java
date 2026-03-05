package com.example.videogamesshop.dto.library;

import com.example.videogamesshop.dto.game.GameSimpleDto;
import java.util.List;
import lombok.Data;

@Data
public class LibraryFullResponse {
    private Long id;
    private String username;
    private List<GameSimpleDto> games;
}