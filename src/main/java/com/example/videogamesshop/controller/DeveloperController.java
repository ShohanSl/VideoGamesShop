package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.developer.DeveloperCatalogResponse;
import com.example.videogamesshop.dto.developer.DeveloperCreateRequest;
import com.example.videogamesshop.dto.developer.DeveloperFullResponse;
import com.example.videogamesshop.dto.developer.DeveloperUpdateRequest;
import com.example.videogamesshop.dto.developer.DeveloperWithGamesRequest;
import com.example.videogamesshop.dto.developer.DeveloperWithGamesResponse;
import com.example.videogamesshop.service.DeveloperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/developers")
@RequiredArgsConstructor
@Validated
@Tag(name = "Developers", description = "Operations for managing developers")
public class DeveloperController {

    private final DeveloperService developerService;

    @GetMapping
    @Operation(summary = "Get all developers")
    public List<DeveloperCatalogResponse> getAllDevelopers() {
        return developerService.getAllDevelopers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get developer by id")
    public DeveloperFullResponse getDeveloperById(
            @PathVariable @Positive(message = "Id must be positive") Long id) {
        return developerService.getDeveloperById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new developer")
    public DeveloperFullResponse createDeveloper(
            @Valid @RequestBody DeveloperCreateRequest request) {
        return developerService.createDeveloper(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update developer by id")
    public DeveloperFullResponse updateDeveloper(
            @PathVariable @Positive(message = "Id must be positive") Long id,
            @Valid @RequestBody DeveloperUpdateRequest request) {
        return developerService.updateDeveloper(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete developer by id")
    public void deleteDeveloper(@PathVariable @Positive(message = "Id must be positive") Long id) {
        developerService.deleteDeveloper(id);
    }

    @PostMapping("/{developerId}/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Attach a game to a developer")
    public void addGameToDeveloper(
            @PathVariable @Positive(message = "Developer id must be positive") Long developerId,
            @PathVariable @Positive(message = "Game id must be positive") Long gameId) {
        developerService.addGameToDeveloper(developerId, gameId);
    }

    @DeleteMapping("/{developerId}/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Detach a game from a developer")
    public void removeGameFromDeveloper(
            @PathVariable @Positive(message = "Developer id must be positive") Long developerId,
            @PathVariable @Positive(message = "Game id must be positive") Long gameId) {
        developerService.removeGameFromDeveloper(developerId, gameId);
    }

    @PostMapping("/with-games/without-tx")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create developer with games without transaction")
    public DeveloperWithGamesResponse createDeveloperWithGamesWithoutTransaction(
            @Valid @RequestBody DeveloperWithGamesRequest request) {
        return developerService.createDeveloperWithGamesWithoutTransaction(
                request.getDeveloper(), request.getGames());
    }

    @PostMapping("/with-games/with-tx")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create developer with games in transaction")
    public DeveloperWithGamesResponse createDeveloperWithGamesWithTransaction(
            @Valid @RequestBody DeveloperWithGamesRequest request) {
        return developerService.createDeveloperWithGamesWithTransaction(
                request.getDeveloper(), request.getGames());
    }
}
