package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.game.GameCatalogResponse;
import com.example.videogamesshop.dto.game.GameFullResponse;
import com.example.videogamesshop.dto.game.GameRequest;
import com.example.videogamesshop.dto.game.GameUpdateRequest;
import com.example.videogamesshop.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
@Validated
@Tag(name = "Games", description = "Operations for managing games")
public class GameController {
    private final GameService gameService;

    @GetMapping
    @Operation(summary = "Get games catalog with optional category filter")
    public Page<GameCatalogResponse> getCatalog(
            @RequestParam(required = false) List<@Positive
                    (message = "Category id must be positive") Long> categoryIds,
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {

        if (categoryIds != null && !categoryIds.isEmpty()) {
            return gameService.getCatalogByCategories(categoryIds, pageable);
        }
        return gameService.getAllCatalog(pageable);
    }

    @GetMapping("/by-publisher")
    @Operation(summary = "Get games by publisher name")
    public List<GameCatalogResponse> getGamesByPublisher(
            @RequestParam @NotBlank(message = "Publisher name is required") String publisherName) {
        return gameService.getCatalogByPublisher(publisherName);
    }

    @GetMapping("/N+1")
    @Operation(summary = "Get catalog endpoint demonstrating N+1 problem")
    public List<GameCatalogResponse> getCatalogWithTrouble() {
        return gameService.getCatalogWithTrouble();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get game by id")
    public GameFullResponse getGameById(
            @PathVariable @Positive(message = "Id must be positive") Long id) {
        return gameService.getGameById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new game")
    public GameFullResponse createGame(@Valid @RequestBody GameRequest request) {
        return gameService.createGame(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update game by id")
    public GameFullResponse updateGame(
            @PathVariable @Positive(message = "Id must be positive") Long id,
            @Valid @RequestBody GameUpdateRequest request) {
        return gameService.updateGame(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete game by id")
    public void deleteGame(@PathVariable @Positive(message = "Id must be positive") Long id) {
        gameService.deleteGame(id);
    }
}
