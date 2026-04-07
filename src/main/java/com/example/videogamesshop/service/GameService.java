package com.example.videogamesshop.service;

import com.example.videogamesshop.cache.GameCacheService;
import com.example.videogamesshop.cache.GameQueryKey;
import com.example.videogamesshop.dto.game.GameCatalogResponse;
import com.example.videogamesshop.dto.game.GameFullResponse;
import com.example.videogamesshop.dto.game.GameRequest;
import com.example.videogamesshop.dto.game.GameUpdateRequest;
import com.example.videogamesshop.entity.Category;
import com.example.videogamesshop.entity.Developer;
import com.example.videogamesshop.entity.Game;
import com.example.videogamesshop.entity.Publisher;
import com.example.videogamesshop.entity.User;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final DeveloperRepository developerRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final GameMapper gameMapper;
    private final GameCacheService cacheService;

    public Page<GameCatalogResponse> getAllCatalog(Pageable pageable) {
        GameQueryKey key = new GameQueryKey(null, pageable);
        Page<GameCatalogResponse> cached = cacheService.get(key);
        if (cached != null) {
            return cached;
        }
        Page<GameCatalogResponse> result = gameRepository.findAllWithDetails(pageable)
                .map(gameMapper::toCatalogResponse);
        cacheService.put(key, result);
        return result;
    }

    public Page<GameCatalogResponse> getCatalogByCategories(List<Long> categoryIds,
                                                            Pageable pageable) {
        GameQueryKey key = new GameQueryKey(categoryIds, pageable);
        Page<GameCatalogResponse> cached = cacheService.get(key);
        if (cached != null) {
            return cached;
        }
        Page<GameCatalogResponse> result = gameRepository.findByCategoriesWithDetails(categoryIds,
                        pageable)
                .map(gameMapper::toCatalogResponse);
        cacheService.put(key, result);
        return result;
    }

    public List<GameCatalogResponse> getCatalogByPublisher(String publisherName) {
        return gameRepository.findByPublisherNameNative(publisherName).stream()
                .map(gameMapper::toCatalogResponse)
                .toList();
    }

    public GameFullResponse getGameById(Long id) {
        return gameRepository.findById(id)
                .map(gameMapper::toFullResponse)
                .orElseThrow(() -> new GameNotFoundException(id));
    }

    public GameFullResponse createGame(GameRequest request) {
        Developer developer = developerRepository.findById(request.getDeveloperId())
                .orElseThrow(() -> new DeveloperNotFoundException(
                        "developerId", request.getDeveloperId()));
        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new PublisherNotFoundException(
                        "publisherId", request.getPublisherId()));
        Game game = gameMapper.toEntity(request);
        game.setDeveloper(developer);
        game.setPublisher(publisher);
        developer.getGames().add(game);
        publisher.getGames().add(game);
        Set<Category> categories = request.getCategoryIds().stream()
                .map(catId -> categoryRepository.findById(catId)
                        .orElseThrow(() -> new CategoryNotFoundException("categoryIds", catId)))
                .collect(Collectors.toSet());
        game.setCategories(categories);
        categories.forEach(cat -> cat.getGames().add(game));
        Game savedGame = gameRepository.save(game);
        cacheService.clear();
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
                            .orElseThrow(() -> new CategoryNotFoundException("categoryIds", catId)))
                    .collect(Collectors.toSet());
            existingGame.setCategories(newCategories);
            newCategories.forEach(cat -> cat.getGames().add(existingGame));
        }

        gameMapper.updateEntity(existingGame, request);
        cacheService.clear();
        return gameMapper.toFullResponse(existingGame);
    }

    public void deleteGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
        if (game.getDeveloper() != null) {
            game.getDeveloper().removeGame(game);
        }
        if (game.getPublisher() != null) {
            game.getPublisher().removeGame(game);
        }
        if (game.getCategories() != null) {
            Set<Category> categories = Set.copyOf(game.getCategories());
            categories.forEach(cat -> cat.getGames().remove(game));
            game.getCategories().clear();
        }
        for (User user : Set.copyOf(game.getLibraries())) {
            user.removeGame(game);
        }
        gameRepository.delete(game);
        cacheService.clear();
    }

    public List<GameCatalogResponse> getCatalogWithTrouble() {
        return gameRepository.findAll().stream()
                .map(gameMapper::toCatalogResponse)
                .toList();
    }
}
