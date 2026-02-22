package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.GameCatalogResponse;
import com.example.videogamesshop.dto.GameFullResponse;
import com.example.videogamesshop.service.GameService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<GameCatalogResponse> getCatalog(
            @RequestParam(required = false) String genre) {
        if (genre != null) {
            return gameService.getCatalogByGenre(genre);
        }
        return gameService.getAllCatalog();
    }

    @GetMapping("/{id}")
    public GameFullResponse getGameById(
            @PathVariable Long id) {
        return gameService.getGameById(id);
    }
}
