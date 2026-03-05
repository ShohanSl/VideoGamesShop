package com.example.videogamesshop.service;

import com.example.videogamesshop.dto.developer.DeveloperCatalogResponse;
import com.example.videogamesshop.dto.developer.DeveloperCreateRequest;
import com.example.videogamesshop.dto.developer.DeveloperFullResponse;
import com.example.videogamesshop.dto.developer.DeveloperUpdateRequest;
import com.example.videogamesshop.dto.game.GameRequest;
import com.example.videogamesshop.entity.Developer;
import com.example.videogamesshop.entity.Game;
import com.example.videogamesshop.exception.DeveloperNotFoundException;
import com.example.videogamesshop.exception.GameNotFoundException;
import com.example.videogamesshop.mapper.DeveloperMapper;
import com.example.videogamesshop.mapper.GameMapper;
import com.example.videogamesshop.repository.DeveloperRepository;
import com.example.videogamesshop.repository.GameRepository;
import java.util.List;
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
    private final DeveloperMapper developerMapper;
    private final GameMapper gameMapper;

    public List<DeveloperCatalogResponse> getAllDevelopers() {
        return developerRepository.findAll().stream()
                .map(developerMapper::toCatalogResponse)
                .toList();
    }

    public DeveloperFullResponse getDeveloperById(Long id) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException(id));
        return developerMapper.toFullResponse(developer);
    }

    public DeveloperFullResponse createDeveloper(DeveloperCreateRequest request) {
        Developer developer = DeveloperMapper.toEntity(request);
        Developer savedDeveloper = developerRepository.save(developer);
        return developerMapper.toFullResponse(savedDeveloper);
    }

    public DeveloperFullResponse updateDeveloper(Long id, DeveloperUpdateRequest request) {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException(id));
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
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new DeveloperNotFoundException(developerId));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        developer.addGame(game);
    }

    public void removeGameFromDeveloper(Long developerId, Long gameId) {
        Developer developer = developerRepository.findById(developerId)
                .orElseThrow(() -> new DeveloperNotFoundException(developerId));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        developer.removeGame(game);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void createDeveloperWithGamesWithoutTransaction(DeveloperCreateRequest devRequest,
                                                           List<GameRequest> gameRequests) {
        Developer developer = DeveloperMapper.toEntity(devRequest);
        developer = developerRepository.save(developer);
        for (GameRequest gameRequest : gameRequests) {
            Game game = gameMapper.toEntity(gameRequest);
            game.setDeveloper(developer);
            gameRepository.save(game);
        }
    }

    @Transactional
    public void createDeveloperWithGamesWithTransaction(DeveloperCreateRequest devRequest,
                                                        List<GameRequest> gameRequests) {
        Developer developer = DeveloperMapper.toEntity(devRequest);
        developer = developerRepository.save(developer);
        for (GameRequest gameRequest : gameRequests) {
            Game game = gameMapper.toEntity(gameRequest);
            game.setDeveloper(developer);
            gameRepository.save(game);
        }
    }
}