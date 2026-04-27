package com.example.videogamesshop.mapper;

import com.example.videogamesshop.dto.category.CategoryDto;
import com.example.videogamesshop.dto.game.GameCatalogResponse;
import com.example.videogamesshop.dto.user.UserCreateRequest;
import com.example.videogamesshop.dto.user.UserFullResponse;
import com.example.videogamesshop.dto.user.UserShortResponse;
import com.example.videogamesshop.dto.user.UserUpdateRequest;
import com.example.videogamesshop.entity.Category;
import com.example.videogamesshop.entity.Game;
import com.example.videogamesshop.entity.User;
import java.util.List;
import java.util.Set;

public class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    public static UserShortResponse toShortResponse(User user) {
        UserShortResponse response = new UserShortResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        return response;
    }

    public static UserFullResponse toFullResponse(User user) {
        if (user == null) {
            return null;
        }
        UserFullResponse response = new UserFullResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());

        if (user.getGames() != null && !user.getGames().isEmpty()) {
            List<GameCatalogResponse> gameDtos = user.getGames().stream()
                    .map(UserMapper::toGameCatalogResponse)
                    .toList();
            response.setGames(gameDtos);
        } else {
            response.setGames(List.of());
        }
        return response;
    }

    public static User toEntity(UserCreateRequest request) {
        if (request == null) {
            return null;
        }
        User user = new User();
        user.setUsername(request.getUsername());
        return user;
    }

    public static void updateEntity(User user, UserUpdateRequest request) {
        if (request == null) {
            return;
        }
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
    }

    private static GameCatalogResponse toGameCatalogResponse(Game game) {
        GameCatalogResponse response = new GameCatalogResponse();
        response.setId(game.getId());
        response.setTitle(game.getTitle());
        response.setPrice(game.getPrice());
        response.setReleaseDate(game.getReleaseDate());
        response.setDescription(game.getDescription());
        if (game.getDeveloper() != null) {
            response.setDeveloperId(game.getDeveloper().getId());
            response.setDeveloperName(game.getDeveloper().getName());
        }
        if (game.getPublisher() != null) {
            response.setPublisherId(game.getPublisher().getId());
            response.setPublisherName(game.getPublisher().getName());
        }
        response.setCategories(mapCategories(game.getCategories()));
        return response;
    }

    private static List<CategoryDto> mapCategories(Set<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return List.of();
        }
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }
}
