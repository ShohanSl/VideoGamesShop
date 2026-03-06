package com.example.videogamesshop.service;

import com.example.videogamesshop.dto.game.GameCatalogResponse;
import com.example.videogamesshop.dto.game.GameFullResponse;
import com.example.videogamesshop.dto.game.GameRequest;
import com.example.videogamesshop.dto.game.GameUpdateRequest;
import com.example.videogamesshop.entity.Category;
import com.example.videogamesshop.entity.Developer;
import com.example.videogamesshop.entity.Game;
import com.example.videogamesshop.entity.User;
import com.example.videogamesshop.entity.Publisher;
import com.example.videogamesshop.exception.CategoryNotFoundException;
import com.example.videogamesshop.exception.DeveloperNotFoundException;
import com.example.videogamesshop.exception.GameNotFoundException;
import com.example.videogamesshop.exception.PublisherNotFoundException;
import com.example.videogamesshop.mapper.GameMapper;
import com.example.videogamesshop.repository.CategoryRepository;
import com.example.videogamesshop.repository.DeveloperRepository;
import com.example.videogamesshop.repository.GameRepository;
import com.example.videogamesshop.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final GameMapper gameMapper;

    public List<GameCatalogResponse> getAllCatalog() {
        return gameRepository.findAllWithCategories().stream()
                .map(gameMapper::toCatalogResponse)
                .toList();
    }

    public GameFullResponse getGameById(Long id) {
        return gameRepository.findById(id)
                .map(gameMapper::toFullResponse)
                .orElseThrow(() -> new GameNotFoundException(id));
    }

    public List<GameCatalogResponse> getCatalogByCategories(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return getAllCatalog();
        }
        long categoryCount = categoryIds.size();
        List<Game> games = gameRepository.findByCategories(categoryIds, categoryCount);
        return games.stream()
                .map(gameMapper::toCatalogResponse)
                .toList();
    }

    public GameFullResponse createGame(GameRequest request) {
        Developer developer = developerRepository.findById(request.getDeveloperId())
                .orElseThrow(() -> new DeveloperNotFoundException(request.getDeveloperId()));
        Publisher publisher = null;
        if (request.getPublisherId() != null) {
            publisher = publisherRepository.findById(request.getPublisherId())
                    .orElseThrow(() -> new PublisherNotFoundException(request.getPublisherId()));
        }
        Game game = gameMapper.toEntity(request);
        game.setDeveloper(developer);
        game.setPublisher(publisher);
        developer.getGames().add(game);
        if (publisher != null) {
            publisher.getGames().add(game);
        }
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            Set<Category> categories = request.getCategoryIds().stream()
                    .map(catId -> categoryRepository.findById(catId)
                            .orElseThrow(() -> new CategoryNotFoundException(catId)))
                    .collect(Collectors.toSet());
            game.setCategories(categories);
            categories.forEach(cat -> cat.getGames().add(game));
        }
        Game savedGame = gameRepository.save(game);
        return gameMapper.toFullResponse(savedGame);
    }

    @Transactional
    public GameFullResponse updateGame(Long id, GameUpdateRequest request) {
        Game existingGame = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
        if (request.getCategoryIds() != null) {
            if (existingGame.getCategories() != null) {
                existingGame.getCategories().forEach(cat -> cat.getGames().remove(existingGame));
            }
            Set<Category> newCategories = request.getCategoryIds().stream()
                    .map(catId -> categoryRepository.findById(catId)
                            .orElseThrow(() -> new CategoryNotFoundException(catId)))
                    .collect(Collectors.toSet());
            existingGame.setCategories(newCategories);
            newCategories.forEach(cat -> cat.getGames().add(existingGame));
        }

        gameMapper.updateEntity(existingGame, request);
        return gameMapper.toFullResponse(existingGame);
    }

    public void deleteGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
        if (game.getDeveloper() != null) {
            game.getDeveloper().getGames().remove(game);
        }
        if (game.getCategories() != null) {
            game.getCategories().forEach(cat -> cat.getGames().remove(game));
        }
        for (User user : game.getLibraries()) {
            user.getGames().remove(game);
        }
        gameRepository.delete(game);
    }
}