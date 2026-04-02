package com.example.videogamesshop.service;

import com.example.videogamesshop.dto.developer.DeveloperBulkGameRequest;
import com.example.videogamesshop.dto.developer.DeveloperCatalogResponse;
import com.example.videogamesshop.dto.developer.DeveloperCreateRequest;
import com.example.videogamesshop.dto.developer.DeveloperFullResponse;
import com.example.videogamesshop.dto.developer.DeveloperUpdateRequest;
import com.example.videogamesshop.dto.developer.DeveloperWithGamesResponse;
import com.example.videogamesshop.dto.game.GameFullResponse;
import com.example.videogamesshop.entity.Category;
import com.example.videogamesshop.entity.Developer;
import com.example.videogamesshop.entity.Game;
import com.example.videogamesshop.entity.Publisher;
import com.example.videogamesshop.exception.CategoryNotFoundException;
import com.example.videogamesshop.exception.DeveloperNotFoundException;
import com.example.videogamesshop.exception.GameNotFoundException;
import com.example.videogamesshop.exception.PublisherNotFoundException;
import com.example.videogamesshop.mapper.DeveloperMapper;
import com.example.videogamesshop.mapper.GameMapper;
import com.example.videogamesshop.repository.CategoryRepository;
import com.example.videogamesshop.repository.DeveloperRepository;
import com.example.videogamesshop.repository.GameRepository;
import com.example.videogamesshop.repository.PublisherRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeveloperService {

    private final DeveloperRepository developerRepository;
    private final GameRepository gameRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final DeveloperMapper developerMapper;
    private final GameMapper gameMapper;

    public List<DeveloperCatalogResponse> getAllDevelopers() {
        return developerRepository.findAll().stream()
                .map(developerMapper::toCatalogResponse)
                .toList();
    }

    public DeveloperFullResponse getDeveloperById(Long id) {
        return developerMapper.toFullResponse(findDeveloperById(id));
    }

    public DeveloperFullResponse createDeveloper(DeveloperCreateRequest request) {
        Developer developer = DeveloperMapper.toEntity(request);
        Developer savedDeveloper = developerRepository.save(developer);
        return developerMapper.toFullResponse(savedDeveloper);
    }

    public DeveloperFullResponse updateDeveloper(Long id, DeveloperUpdateRequest request) {
        Developer developer = findDeveloperById(id);
        developerMapper.updateEntity(developer, request);
        return developerMapper.toFullResponse(developer);
    }

    public void deleteDeveloper(Long id) {
        if (!developerRepository.existsById(id)) {
            throw new DeveloperNotFoundException(id);
        }
        developerRepository.deleteById(id);
    }

    public void addGameToDeveloper(Long developerId, Long gameId) {
        updateDeveloperGameRelation(developerId, gameId, true);
    }

    public void removeGameFromDeveloper(Long developerId, Long gameId) {
        updateDeveloperGameRelation(developerId, gameId, false);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public DeveloperWithGamesResponse createDeveloperWithGamesWithoutTransaction(
            DeveloperCreateRequest devRequest, List<DeveloperBulkGameRequest> gameRequests) {
        return createDeveloperWithGames(devRequest, gameRequests);
    }

    @Transactional
    public DeveloperWithGamesResponse createDeveloperWithGamesWithTransaction(
            DeveloperCreateRequest devRequest, List<DeveloperBulkGameRequest> gameRequests) {
        return createDeveloperWithGames(devRequest, gameRequests);
    }

    private DeveloperWithGamesResponse createDeveloperWithGames(
            DeveloperCreateRequest devRequest, List<DeveloperBulkGameRequest> gameRequests) {
        Developer developer = developerRepository.save(DeveloperMapper.toEntity(devRequest));
        List<GameFullResponse> createdGames = gameRequests.stream()
                .map(gameRequest -> createGameForDeveloper(developer, gameRequest))
                .map(gameRepository::save)
                .map(gameMapper::toFullResponse)
                .toList();

        DeveloperWithGamesResponse response = new DeveloperWithGamesResponse();
        response.setDeveloper(developerMapper.toFullResponse(developer));
        response.setGames(createdGames);
        response.setCreatedGamesCount(createdGames.size());
        return response;
    }

    private Game createGameForDeveloper(Developer developer, DeveloperBulkGameRequest request) {
        Publisher publisher = findPublisherById(request.getPublisherId());
        Set<Category> categories = request.getCategoryIds().stream()
                .map(this::findCategoryById)
                .collect(Collectors.toSet());

        Game game = new Game();
        game.setTitle(request.getTitle());
        game.setPrice(request.getPrice());
        game.setReleaseDate(request.getReleaseDate());
        game.setDescription(request.getDescription());
        game.setDeveloper(developer);
        game.setPublisher(publisher);
        game.setCategories(categories);

        developer.getGames().add(game);
        publisher.getGames().add(game);
        categories.forEach(category -> category.getGames().add(game));
        return game;
    }

    private void updateDeveloperGameRelation(Long developerId, Long gameId, boolean attachGame) {
        Developer developer = findDeveloperById(developerId);
        Game game = findGameById(gameId);
        if (attachGame) {
            developer.addGame(game);
            return;
        }
        developer.removeGame(game);
    }

    private Developer findDeveloperById(Long id) {
        return developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException(id));
    }

    private Game findGameById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("categoryIds", id));
    }

    private Publisher findPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException("publisherId", id));
    }
}
