package com.example.videogamesshop.service;

import com.example.videogamesshop.dto.GameCatalogResponse;
import com.example.videogamesshop.dto.GameFullResponse;
import com.example.videogamesshop.mapper.GameMapper;
import com.example.videogamesshop.repository.GameRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Autowired
    public GameService(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    public List<GameCatalogResponse> getAllCatalog() {
        return gameRepository.findAll().stream()
                .map(gameMapper::toCatalogResponse)
                .toList();
    }

    public List<GameCatalogResponse> getCatalogByGenre(String genre) {
        return gameRepository.findByGenre(genre).stream()
                .map(gameMapper::toCatalogResponse)
                .toList();
    }

    public GameFullResponse getGameById(Long id) {
        return gameRepository.findById(id)
                .map(gameMapper::toFullResponse)
                .orElseThrow(() -> new RuntimeException("Bad id error"));
    }
}
