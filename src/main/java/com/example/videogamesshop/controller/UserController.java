package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.user.UserCreateRequest;
import com.example.videogamesshop.dto.user.UserFullResponse;
import com.example.videogamesshop.dto.user.UserUpdateRequest;
import com.example.videogamesshop.service.UserService;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserFullResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserFullResponse createUser(@Valid @RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/{id}")
    public UserFullResponse updateUser(@PathVariable Long id,
                                       @Valid @RequestBody UserUpdateRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{userId}/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addGameToUser(@PathVariable Long userId, @PathVariable Long gameId) {
        userService.addGameToUser(userId, gameId);
    }

    @DeleteMapping("/{userId}/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGameFromUser(@PathVariable Long userId, @PathVariable Long gameId) {
        userService.removeGameFromUser(userId, gameId);
    }
}