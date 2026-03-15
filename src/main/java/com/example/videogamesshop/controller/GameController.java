package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.game.GameCatalogResponse;
import com.example.videogamesshop.dto.game.GameFullResponse;
import com.example.videogamesshop.dto.game.GameRequest;
import com.example.videogamesshop.dto.game.GameUpdateRequest;
import com.example.videogamesshop.service.GameService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping
    public Page<GameCatalogResponse> getCatalog(
            @RequestParam(required = false) List<Long> categoryIds,
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {

        if (categoryIds != null && !categoryIds.isEmpty()) {
            return gameService.getCatalogByCategories(categoryIds, pageable);
        }
        return gameService.getAllCatalog(pageable);
    }

    @GetMapping("/by-publisher")
    public List<GameCatalogResponse> getGamesByPublisher(
            @RequestParam Long publisherId) {
        return gameService.getCatalogByPublisher(publisherId);
    }

    @GetMapping("/N+1")
    public List<GameCatalogResponse> getCatalogWithTrouble() {
        return gameService.getCatalogWithTrouble();
    }

    @GetMapping("/{id}")
    public GameFullResponse getGameById(@PathVariable Long id) {
        return gameService.getGameById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameFullResponse createGame(@RequestBody GameRequest request) {
        return gameService.createGame(request);
    }

    @PutMapping("/{id}")
    public GameFullResponse updateGame(@PathVariable Long id,
                                       @RequestBody GameUpdateRequest request) {
        return gameService.updateGame(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
    }
}