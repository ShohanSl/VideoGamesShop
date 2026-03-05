package com.example.videogamesshop.mapper;

import com.example.videogamesshop.dto.developer.DeveloperCatalogResponse;
import com.example.videogamesshop.dto.developer.DeveloperCreateRequest;
import com.example.videogamesshop.dto.developer.DeveloperFullResponse;
import com.example.videogamesshop.dto.developer.DeveloperUpdateRequest;
import com.example.videogamesshop.dto.game.GameSimpleDto;
import com.example.videogamesshop.entity.Developer;
import com.example.videogamesshop.entity.Game;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DeveloperMapper {

    public DeveloperCatalogResponse toCatalogResponse(Developer developer) {
        if (developer == null) {
            return null;
        }
        DeveloperCatalogResponse response = new DeveloperCatalogResponse();
        response.setId(developer.getId());
        response.setName(developer.getName());
        response.setCountry(developer.getCountry());
        response.setFoundedDate(developer.getFoundedDate());
        return response;
    }

    public DeveloperFullResponse toFullResponse(Developer developer) {
        if (developer == null) {
            return null;
        }
        DeveloperFullResponse response = new DeveloperFullResponse();
        response.setId(developer.getId());
        response.setName(developer.getName());
        response.setCountry(developer.getCountry());
        response.setFoundedDate(developer.getFoundedDate());

        List<Game> games = developer.getGames();
        if (games != null && !games.isEmpty()) {
            List<GameSimpleDto> gameDtos = games.stream()
                    .map(GameMapper::toGameSimpleDto)
                    .toList();
            response.setGames(gameDtos);
        } else {
            response.setGames(Collections.emptyList());
        }
        return response;
    }

    public static Developer toEntity(DeveloperCreateRequest request) {
        if (request == null) {
            return null;
        }
        Developer developer = new Developer();
        developer.setName(request.getName());
        developer.setCountry(request.getCountry());
        developer.setFoundedDate(request.getFoundedDate());
        return developer;
    }

    public void updateEntity(Developer developer, DeveloperUpdateRequest request) {
        if (developer == null || request == null) {
            return;
        }
        if (request.getName() != null) {
            developer.setName(request.getName());
        }
        if (request.getCountry() != null) {
            developer.setCountry(request.getCountry());
        }
        if (request.getFoundedDate() != null) {
            developer.setFoundedDate(request.getFoundedDate());
        }
    }
}