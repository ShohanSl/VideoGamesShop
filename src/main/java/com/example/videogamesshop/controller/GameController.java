package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.GameCatalogResponse;
import com.example.videogamesshop.dto.GameFullResponse;
import com.example.videogamesshop.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping
    public List<GameCatalogResponse> getCatalog(
            @RequestParam(required = false) String genre) {
        if (genre != null) {
            return gameService.getCatalogByGenre(genre);
        }
        return gameService.getAllCatalog();
    }

    @GetMapping("/{id}")
    public GameFullResponse getGameById(@PathVariable Long id) {
        return gameService.getGameById(id);
    }
}