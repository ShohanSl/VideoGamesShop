package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.user.UserCreateRequest;
import com.example.videogamesshop.dto.user.UserFullResponse;
import com.example.videogamesshop.dto.user.UserShortResponse;
import com.example.videogamesshop.dto.user.UserUpdateRequest;
import com.example.videogamesshop.security.JwtPrincipal;
import com.example.videogamesshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "Users", description = "Operations for managing users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users")
    public List<UserShortResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public UserFullResponse getUserById(
            @PathVariable @Positive(message = "Id must be positive") Long id,
            @AuthenticationPrincipal JwtPrincipal principal) {
        ensureUserAccess(id, principal);
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user")
    public UserFullResponse createUser(@Valid @RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by id")
    public UserFullResponse updateUser(
            @PathVariable @Positive(message = "Id must be positive") Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user by id")
    public void deleteUser(@PathVariable @Positive(message = "Id must be positive") Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{userId}/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Add a game to user library")
    public void addGameToUser(
            @PathVariable @Positive(message = "User id must be positive") Long userId,
            @PathVariable @Positive(message = "Game id must be positive") Long gameId,
            @AuthenticationPrincipal JwtPrincipal principal) {
        ensureUserAccess(userId, principal);
        userService.addGameToUser(userId, gameId);
    }

    @DeleteMapping("/{userId}/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a game from user library")
    public void removeGameFromUser(
            @PathVariable @Positive(message = "User id must be positive") Long userId,
            @PathVariable @Positive(message = "Game id must be positive") Long gameId,
            @AuthenticationPrincipal JwtPrincipal principal) {
        ensureUserAccess(userId, principal);
        userService.removeGameFromUser(userId, gameId);
    }

    private void ensureUserAccess(Long userId, JwtPrincipal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Authentication is required");
        }
        if (principal.isAdmin()) {
            return;
        }
        if (principal.userId() != null && principal.userId().equals(userId)) {
            return;
        }
        throw new AccessDeniedException("Access is denied");
    }
}
