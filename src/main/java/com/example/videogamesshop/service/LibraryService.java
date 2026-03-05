package com.example.videogamesshop.service;

import com.example.videogamesshop.dto.library.LibraryCreateRequest;
import com.example.videogamesshop.dto.library.LibraryFullResponse;
import com.example.videogamesshop.dto.library.LibraryUpdateRequest;
import com.example.videogamesshop.entity.Game;
import com.example.videogamesshop.entity.Library;
import com.example.videogamesshop.exception.GameNotFoundException;
import com.example.videogamesshop.exception.LibraryNotFoundException;
import com.example.videogamesshop.mapper.LibraryMapper;
import com.example.videogamesshop.repository.GameRepository;
import com.example.videogamesshop.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final GameRepository gameRepository;

    public LibraryFullResponse getUserById(Long id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException(id));
        return LibraryMapper.toFullResponse(library);
    }

    public LibraryFullResponse createUser(LibraryCreateRequest request) {
        Library library = LibraryMapper.toEntity(request);
        Library saved = libraryRepository.save(library);
        return LibraryMapper.toFullResponse(saved);
    }

    public LibraryFullResponse updateUser(Long id, LibraryUpdateRequest request) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException(id));
        LibraryMapper.updateEntity(library, request);
        return LibraryMapper.toFullResponse(library);
    }

    public void deleteUser(Long id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException(id));
        for (Game game : library.getGames()) {
            game.getLibraries().remove(library);
        }
        libraryRepository.delete(library);
    }

    public void addGameToUser(Long userId, Long gameId) {
        Library library = libraryRepository.findById(userId)
                .orElseThrow(() -> new LibraryNotFoundException(userId));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        library.addGame(game);
    }

    public void removeGameFromUser(Long userId, Long gameId) {
        Library library = libraryRepository.findById(userId)
                .orElseThrow(() -> new LibraryNotFoundException(userId));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        library.removeGame(game);
    }
}