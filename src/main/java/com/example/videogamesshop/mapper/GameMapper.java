package com.example.videogamesshop.mapper;

import com.example.videogamesshop.dto.GameCatalogResponse;
import com.example.videogamesshop.dto.GameFullResponse;
import com.example.videogamesshop.entity.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    public GameCatalogResponse toCatalogResponse(Game game) {
        return new GameCatalogResponse(
                game.getId(),
                game.getTitle(),
                game.getGenre(),
                game.getPrice()
        );
    }

    public GameFullResponse toFullResponse(Game game) {
        return new GameFullResponse(
                game.getId(),
                game.getTitle(),
                game.getGenre(),
                game.getPrice(),
                game.getReleaseDate(),
                game.getDescription()
        );
    }
}
