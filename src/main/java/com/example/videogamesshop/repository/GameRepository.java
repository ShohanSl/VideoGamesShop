package com.example.videogamesshop.repository;

import com.example.videogamesshop.entity.Game;
import java.util.List;
import java.util.Optional;

public interface GameRepository {
    List<Game> findAll();
    List<Game> findByGenre(String genre);
    Optional<Game> findById(Long id);
}