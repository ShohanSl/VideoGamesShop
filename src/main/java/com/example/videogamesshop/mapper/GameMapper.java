package com.example.videogamesshop.mapper;

import com.example.videogamesshop.dto.category.CategoryDto;
import com.example.videogamesshop.dto.game.GameCatalogResponse;
import com.example.videogamesshop.dto.game.GameFullResponse;
import com.example.videogamesshop.dto.game.GameRequest;
import com.example.videogamesshop.dto.game.GameSimpleDto;
import com.example.videogamesshop.dto.game.GameUpdateRequest;
import com.example.videogamesshop.entity.Category;
import com.example.videogamesshop.entity.Game;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameMapper {

    public GameCatalogResponse toCatalogResponse(Game game) {
        if (game == null) {
            return null;
        }
        GameCatalogResponse response = new GameCatalogResponse();
        response.setId(game.getId());
        response.setTitle(game.getTitle());
        response.setPrice(game.getPrice());
        response.setCategories(mapCategories(game));
        return response;
    }

    public GameFullResponse toFullResponse(Game game) {
        if (game == null) {
            return null;
        }
        GameFullResponse response = new GameFullResponse();
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
        response.setCategories(mapCategories(game));
        return response;
    }

    public Game toEntity(GameRequest request) {
        Game game = new Game();
        game.setTitle(request.getTitle());
        game.setPrice(request.getPrice());
        game.setReleaseDate(request.getReleaseDate());
        game.setDescription(request.getDescription());
        return game;
    }

    public void updateEntity(Game game, GameUpdateRequest request) {
        if (request.getTitle() != null) {
            game.setTitle(request.getTitle());
        }
        if (request.getPrice() != null) {
            game.setPrice(request.getPrice());
        }
        if (request.getReleaseDate() != null) {
            game.setReleaseDate(request.getReleaseDate());
        }
        if (request.getDescription() != null) {
            game.setDescription(request.getDescription());
        }
    }

    public static GameSimpleDto toGameSimpleDto(Game game) {
        if (game == null) {
            return null;
        }
        GameSimpleDto dto = new GameSimpleDto();
        dto.setId(game.getId());
        dto.setTitle(game.getTitle());
        return dto;
    }

    private List<CategoryDto> mapCategories(Game game) {
        Set<Category> categories = game.getCategories();
        if (categories != null && !categories.isEmpty()) {
            return categories.stream()
                    .map(CategoryMapper::toCategoryDto)
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }
}
