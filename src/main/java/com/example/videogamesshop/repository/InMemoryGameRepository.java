package com.example.videogamesshop.repository;

import com.example.videogamesshop.entity.Game;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryGameRepository implements GameRepository {
    private final List<Game> games = new ArrayList<>();
    private static final String SHOOTER = "Shooter";

    public InMemoryGameRepository() {
        gamesListInit();
    }

    private void gamesListInit() {
        games.add(new Game(1L, "The Witcher 3: Wild Hunt", "RPG",
                29.99, LocalDate.of(2015, 5, 19), null));
        games.add(new Game(2L, "Counter-Strike 2", SHOOTER,
                0.0, LocalDate.of(2023, 9, 27), null));
        games.add(new Game(3L, "Dota 2", "MOBA",
                0.0, LocalDate.of(2013, 7, 9), null));
        games.add(new Game(4L, "Cyberpunk 2077", "RPG",
                49.99, LocalDate.of(2020, 12, 10), null));
        games.add(new Game(5L, "Red Dead Redemption 2", "Action",
                59.99, LocalDate.of(2019, 12, 5), null));
        games.add(new Game(6L, "Grand Theft Auto V", "Action",
                29.99, LocalDate.of(2015, 4, 14), null));
        games.add(new Game(7L, "PUBG: Battlegrounds", SHOOTER,
                29.99, LocalDate.of(2017, 12, 21), null));
        games.add(new Game(8L, "Apex Legends", SHOOTER,
                0.0, LocalDate.of(2019, 2, 4), null));
        games.add(new Game(9L, "Path of Exile", "RPG",
                0.0, LocalDate.of(2013, 10, 23), null));
        games.add(new Game(10L, "Rust", "Survival",
                39.99, LocalDate.of(2018, 2, 8), null));
    }

    @Override
    public List<Game> findAll() {
        return new ArrayList<>(games);
    }

    @Override
    public List<Game> findByGenre(String genre) {
        return new ArrayList<>(games.stream()
                .filter(game -> game.getGenre().equals(genre))
                .toList());
    }

    @Override
    public Optional<Game> findById(Long id) {
        return games.stream()
                .filter(game -> game.getId().equals(id))
                .findFirst();
    }
}
