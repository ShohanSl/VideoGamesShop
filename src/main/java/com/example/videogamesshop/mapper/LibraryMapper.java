package com.example.videogamesshop.mapper;

import com.example.videogamesshop.dto.game.GameSimpleDto;
import com.example.videogamesshop.dto.library.LibraryCreateRequest;
import com.example.videogamesshop.dto.library.LibraryFullResponse;
import com.example.videogamesshop.dto.library.LibraryUpdateRequest;
import com.example.videogamesshop.entity.Library;
import java.util.List;

public class LibraryMapper {

    private LibraryMapper() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    public static LibraryFullResponse toFullResponse(Library library) {
        if (library == null) {
            return null;
        }
        LibraryFullResponse response = new LibraryFullResponse();
        response.setId(library.getId());
        response.setUsername(library.getUsername());

        if (library.getGames() != null && !library.getGames().isEmpty()) {
            List<GameSimpleDto> gameDtos = library.getGames().stream()
                    .map(GameMapper::toGameSimpleDto)
                    .toList();
            response.setGames(gameDtos);
        } else {
            response.setGames(List.of());
        }
        return response;
    }

    public static Library toEntity(LibraryCreateRequest request) {
        if (request == null) {
            return null;
        }
        Library library = new Library();
        library.setUsername(request.getUsername());
        return library;
    }

    public static void updateEntity(Library library, LibraryUpdateRequest request) {
        if (request == null) {
            return;
        }
        if (request.getUsername() != null) {
            library.setUsername(request.getUsername());
        }
    }
}