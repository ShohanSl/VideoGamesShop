package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.library.LibraryCreateRequest;
import com.example.videogamesshop.dto.library.LibraryFullResponse;
import com.example.videogamesshop.dto.library.LibraryUpdateRequest;
import com.example.videogamesshop.service.LibraryService;
import jakarta.validation.Valid;
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
@RequestMapping("/libs")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping("/{id}")
    public LibraryFullResponse getUserById(@PathVariable Long id) {
        return libraryService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LibraryFullResponse createUser(@Valid @RequestBody LibraryCreateRequest request) {
        return libraryService.createUser(request);
    }

    @PutMapping("/{id}")
    public LibraryFullResponse updateUser(@PathVariable Long id,
                                          @Valid @RequestBody LibraryUpdateRequest request) {
        return libraryService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        libraryService.deleteUser(id);
    }

    @PostMapping("/{userId}/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addGameToUser(@PathVariable Long userId, @PathVariable Long gameId) {
        libraryService.addGameToUser(userId, gameId);
    }

    @DeleteMapping("/{userId}/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGameFromUser(@PathVariable Long userId, @PathVariable Long gameId) {
        libraryService.removeGameFromUser(userId, gameId);
    }
}