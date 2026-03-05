package com.example.videogamesshop.mapper;

import com.example.videogamesshop.dto.game.GameSimpleDto;
import com.example.videogamesshop.dto.publisher.PublisherCreateRequest;
import com.example.videogamesshop.dto.publisher.PublisherFullResponse;
import com.example.videogamesshop.entity.Publisher;
import java.util.List;

public class PublisherMapper {

    private PublisherMapper() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    public static PublisherFullResponse toFullResponse(Publisher publisher) {
        if (publisher == null) {
            return null;
        }
        PublisherFullResponse response = new PublisherFullResponse();
        response.setId(publisher.getId());
        response.setName(publisher.getName());
        response.setCountry(publisher.getCountry());
        response.setFoundedDate(publisher.getFoundedDate());

        List<GameSimpleDto> gameDtos = publisher.getGames().stream()
                .map(GameMapper::toGameSimpleDto)
                .toList();
        response.setGames(gameDtos);
        return response;
    }

    public static Publisher toEntity(PublisherCreateRequest request) {
        if (request == null) {
            return null;
        }
        Publisher publisher = new Publisher();
        publisher.setName(request.getName());
        publisher.setCountry(request.getCountry());
        publisher.setFoundedDate(request.getFoundedDate());
        return publisher;
    }

    public static void updateEntity(Publisher publisher, PublisherCreateRequest request) {
        if (request == null) {
            return;
        }
        if (request.getName() != null) {
            publisher.setName(request.getName());
        }
        if (request.getCountry() != null) {
            publisher.setCountry(request.getCountry());
        }
        if (request.getFoundedDate() != null) {
            publisher.setFoundedDate(request.getFoundedDate());
        }
    }
}