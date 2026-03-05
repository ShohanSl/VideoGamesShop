package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.developer.DeveloperCatalogResponse;
import com.example.videogamesshop.dto.developer.DeveloperCreateRequest;
import com.example.videogamesshop.dto.developer.DeveloperFullResponse;
import com.example.videogamesshop.dto.developer.DeveloperUpdateRequest;
import com.example.videogamesshop.dto.developer.DeveloperWithGamesRequest;
import com.example.videogamesshop.service.DeveloperService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class DeveloperController {

    private final DeveloperService developerService;

    @GetMapping
    public List<DeveloperCatalogResponse> getAllDevelopers() {
        return developerService.getAllDevelopers();
    }

    @GetMapping("/{id}")
    public DeveloperFullResponse getDeveloperById(@PathVariable Long id) {
        return developerService.getDeveloperById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeveloperFullResponse createDeveloper(
            @Valid @RequestBody DeveloperCreateRequest request) {
        return developerService.createDeveloper(request);
    }

    @PutMapping("/{id}")
    public DeveloperFullResponse updateDeveloper(
            @PathVariable Long id, @Valid @RequestBody DeveloperUpdateRequest request) {
        return developerService.updateDeveloper(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeveloper(@PathVariable Long id) {
        developerService.deleteDeveloper(id);
    }

    @PostMapping("/{developerId}/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addGameToDeveloper(@PathVariable Long developerId, @PathVariable Long gameId) {
        developerService.addGameToDeveloper(developerId, gameId);
    }

    @DeleteMapping("/{developerId}/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGameFromDeveloper(@PathVariable Long developerId, @PathVariable Long gameId) {
        developerService.removeGameFromDeveloper(developerId, gameId);
    }

    @PostMapping("/with-games/without-tx")
    public void testWithoutTransaction(@RequestBody DeveloperWithGamesRequest request) {
        developerService.createDeveloperWithGamesWithoutTransaction(
                request.getDeveloper(), request.getGames());
    }

    @PostMapping("/with-games/with-tx")
    public void testWithTransaction(@RequestBody DeveloperWithGamesRequest request) {
        developerService.createDeveloperWithGamesWithTransaction(
                request.getDeveloper(), request.getGames());
    }
}