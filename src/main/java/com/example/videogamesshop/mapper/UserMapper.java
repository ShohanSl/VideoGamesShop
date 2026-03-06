package com.example.videogamesshop.mapper;

import com.example.videogamesshop.dto.game.GameSimpleDto;
import com.example.videogamesshop.dto.user.UserCreateRequest;
import com.example.videogamesshop.dto.user.UserFullResponse;
import com.example.videogamesshop.dto.user.UserUpdateRequest;
import com.example.videogamesshop.entity.User;
import java.util.List;

public class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    public static UserFullResponse toFullResponse(User user) {
        if (user == null) {
            return null;
        }
        UserFullResponse response = new UserFullResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());

        if (user.getGames() != null && !user.getGames().isEmpty()) {
            List<GameSimpleDto> gameDtos = user.getGames().stream()
                    .map(GameMapper::toGameSimpleDto)
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
}