package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.videogamesshop.cache.GameCacheService;
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private GameCacheService cacheService;

    @InjectMocks
    private GameService gameService;

    @Test
    void shouldReturnCachedCatalogWhenPresent() {
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<GameCatalogResponse>(List.of());
        when(cacheService.get(any())).thenReturn(page);

        var result = gameService.getAllCatalog(pageable);

        assertSame(page, result);
    }

    @Test
    void shouldLoadCatalogAndCacheItWhenCacheMissed() {
        var pageable = PageRequest.of(0, 10);
        Game game = new Game();
        game.setId(1L);
        GameCatalogResponse response = new GameCatalogResponse();
        response.setId(1L);
        var page = new PageImpl<>(List.of(game), pageable, 1);
        when(cacheService.get(any())).thenReturn(null);
        when(gameRepository.findAllWithDetails(pageable)).thenReturn(page);
        when(gameMapper.toCatalogResponse(game)).thenReturn(response);

        var result = gameService.getAllCatalog(pageable);

        assertEquals(1, result.getContent().size());
        verify(cacheService).put(any(), any());
    }

    @Test
    void shouldReturnCatalogByCategories() {
        var pageable = PageRequest.of(0, 10);
        Game game = new Game();
        game.setId(2L);
        GameCatalogResponse response = new GameCatalogResponse();
        response.setId(2L);
        var page = new PageImpl<>(List.of(game), pageable, 1);
        when(cacheService.get(any())).thenReturn(null);
        when(gameRepository.findByCategoriesWithDetails(List.of(1L, 2L), pageable)).thenReturn(page);
        when(gameMapper.toCatalogResponse(game)).thenReturn(response);

        var result = gameService.getCatalogByCategories(List.of(1L, 2L), pageable);

        assertEquals(1, result.getContent().size());
        verify(cacheService).put(any(), any());
    }

    @Test
    void shouldReturnCachedCatalogByCategoriesWhenPresent() {
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<GameCatalogResponse>(List.of());
        when(cacheService.get(any())).thenReturn(page);

        var result = gameService.getCatalogByCategories(List.of(1L, 2L), pageable);

        assertSame(page, result);
    }

    @Test
    void shouldReturnGamesByPublisher() {
        Game game = new Game();
        game.setId(3L);
        GameCatalogResponse response = new GameCatalogResponse();
        response.setId(3L);
        when(gameRepository.findByPublisherNameNative("Ubisoft")).thenReturn(List.of(game));
        when(gameMapper.toCatalogResponse(game)).thenReturn(response);

        List<GameCatalogResponse> result = gameService.getCatalogByPublisher("Ubisoft");

        assertEquals(1, result.size());
        assertEquals(3L, result.get(0).getId());
    }

    @Test
    void shouldReturnGameById() {
        Game game = new Game();
        game.setId(1L);
        GameFullResponse response = new GameFullResponse();
        response.setId(1L);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameMapper.toFullResponse(game)).thenReturn(response);

        GameFullResponse result = gameService.getGameById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowWhenGameNotFound() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> gameService.getGameById(99L));
    }

    @Test
    void shouldCreateGame() {
        GameRequest request = new GameRequest();
        request.setTitle("Cyberpunk 2077");
        request.setPrice(59.99);
        request.setReleaseDate(LocalDate.of(2020, 12, 10));
        request.setDescription("Open world");
        request.setDeveloperId(1L);
        request.setPublisherId(2L);
        request.setCategoryIds(List.of(3L));

        Developer developer = new Developer();
        developer.setId(1L);
        Publisher publisher = new Publisher();
        publisher.setId(2L);
        Category category = new Category();
        category.setId(3L);
        Game mappedGame = new Game();
        Game savedGame = new Game();
        savedGame.setId(11L);
        GameFullResponse response = new GameFullResponse();
        response.setId(11L);

        when(developerRepository.findById(1L)).thenReturn(Optional.of(developer));
        when(publisherRepository.findById(2L)).thenReturn(Optional.of(publisher));
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));
        when(gameMapper.toEntity(request)).thenReturn(mappedGame);
        when(gameRepository.save(mappedGame)).thenReturn(savedGame);
        when(gameMapper.toFullResponse(savedGame)).thenReturn(response);

        GameFullResponse result = gameService.createGame(request);

        assertEquals(11L, result.getId());
        verify(cacheService).clear();
        assertEquals(1, developer.getGames().size());
        assertEquals(1, publisher.getGames().size());
    }

    @Test
    void shouldThrowWhenCreatingGameWithMissingDeveloper() {
        GameRequest request = new GameRequest();
        request.setDeveloperId(1L);
        when(developerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DeveloperNotFoundException.class,
                () -> gameService.createGame(request));
    }

    @Test
    void shouldThrowWhenCreatingGameWithMissingPublisher() {
        GameRequest request = new GameRequest();
        request.setDeveloperId(1L);
        request.setPublisherId(2L);

        Developer developer = new Developer();
        developer.setId(1L);

        when(developerRepository.findById(1L)).thenReturn(Optional.of(developer));
        when(publisherRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> gameService.createGame(request));
    }

    @Test
    void shouldThrowWhenCreatingGameWithMissingCategory() {
        GameRequest request = new GameRequest();
        request.setDeveloperId(1L);
        request.setPublisherId(2L);
        request.setCategoryIds(List.of(3L));

        Developer developer = new Developer();
        developer.setId(1L);
        Publisher publisher = new Publisher();
        publisher.setId(2L);
        Game mappedGame = new Game();

        when(developerRepository.findById(1L)).thenReturn(Optional.of(developer));
        when(publisherRepository.findById(2L)).thenReturn(Optional.of(publisher));
        when(gameMapper.toEntity(request)).thenReturn(mappedGame);
        when(categoryRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> gameService.createGame(request));
    }

    @Test
    void shouldUpdateCategoriesForGame() {
        Game game = new Game();
        game.setId(1L);
        Category oldCategory = new Category();
        oldCategory.setId(10L);
        oldCategory.setGames(new HashSet<>());
        oldCategory.getGames().add(game);
        game.setCategories(new HashSet<>(Set.of(oldCategory)));

        Category newCategory = new Category();
        newCategory.setId(20L);
        newCategory.setGames(new HashSet<>());

        GameUpdateRequest request = new GameUpdateRequest();
        request.setCategoryIds(List.of(20L));

        GameFullResponse response = new GameFullResponse();
        response.setId(1L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(categoryRepository.findById(20L)).thenReturn(Optional.of(newCategory));
        when(gameMapper.toFullResponse(game)).thenReturn(response);

        GameFullResponse result = gameService.updateGame(1L, request);

        assertEquals(1L, result.getId());
        assertEquals(0, oldCategory.getGames().size());
        assertEquals(1, newCategory.getGames().size());
        verify(cacheService).clear();
    }

    @Test
    void shouldUpdateCategoriesWhenExistingCategoriesAreNull() {
        Game game = new Game();
        game.setId(6L);
        game.setCategories(null);

        Category newCategory = new Category();
        newCategory.setId(30L);
        newCategory.setGames(new HashSet<>());

        GameUpdateRequest request = new GameUpdateRequest();
        request.setCategoryIds(List.of(30L));

        GameFullResponse response = new GameFullResponse();
        response.setId(6L);

        when(gameRepository.findById(6L)).thenReturn(Optional.of(game));
        when(categoryRepository.findById(30L)).thenReturn(Optional.of(newCategory));
        when(gameMapper.toFullResponse(game)).thenReturn(response);

        GameFullResponse result = gameService.updateGame(6L, request);

        assertEquals(6L, result.getId());
        assertEquals(1, game.getCategories().size());
        assertEquals(1, newCategory.getGames().size());
        verify(cacheService).clear();
    }

    @Test
    void shouldUpdateGameWithoutCategoriesWhenCategoryIdsAreNull() {
        Game game = new Game();
        game.setId(2L);
        GameUpdateRequest request = new GameUpdateRequest();
        request.setTitle("Updated title");

        GameFullResponse response = new GameFullResponse();
        response.setId(2L);

        when(gameRepository.findById(2L)).thenReturn(Optional.of(game));
        when(gameMapper.toFullResponse(game)).thenReturn(response);

        GameFullResponse result = gameService.updateGame(2L, request);

        assertEquals(2L, result.getId());
        verify(gameMapper).updateEntity(game, request);
        verify(cacheService).clear();
    }

    @Test
    void shouldThrowWhenUpdatingMissingGame() {
        when(gameRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class,
                () -> gameService.updateGame(999L, new GameUpdateRequest()));
    }

    @Test
    void shouldThrowWhenUpdatingGameWithMissingCategory() {
        Game game = new Game();
        game.setId(3L);
        game.setCategories(new HashSet<>());

        GameUpdateRequest request = new GameUpdateRequest();
        request.setCategoryIds(List.of(123L));

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(categoryRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> gameService.updateGame(3L, request));
    }

    @Test
    void shouldDeleteGameAndRemoveRelations() {
        Game game = new Game();
        game.setId(1L);
        Developer developer = new Developer();
        developer.getGames().add(game);
        game.setDeveloper(developer);

        Category category = new Category();
        category.setGames(new HashSet<>(Set.of(game)));
        game.setCategories(new HashSet<>(Set.of(category)));

        User user = new User();
        user.getGames().add(game);
        game.setLibraries(new HashSet<>(Set.of(user)));

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        gameService.deleteGame(1L);

        assertEquals(0, developer.getGames().size());
        assertEquals(0, category.getGames().size());
        assertEquals(0, user.getGames().size());
        verify(gameRepository).delete(game);
        verify(cacheService).clear();
    }

    @Test
    void shouldDeleteGameWithoutOptionalRelations() {
        Game game = new Game();
        game.setId(5L);
        game.setDeveloper(null);
        game.setCategories(null);
        User user = new User();
        user.getGames().add(game);
        game.setLibraries(new HashSet<>(Set.of(user)));

        when(gameRepository.findById(5L)).thenReturn(Optional.of(game));

        gameService.deleteGame(5L);

        assertEquals(0, user.getGames().size());
        verify(gameRepository).delete(game);
        verify(cacheService).clear();
    }

    @Test
    void shouldThrowWhenDeletingMissingGame() {
        when(gameRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class,
                () -> gameService.deleteGame(404L));
    }

    @Test
    void shouldReturnCatalogWithTrouble() {
        Game game = new Game();
        game.setId(4L);
        GameCatalogResponse response = new GameCatalogResponse();
        response.setId(4L);
        when(gameRepository.findAll()).thenReturn(List.of(game));
        when(gameMapper.toCatalogResponse(game)).thenReturn(response);

        List<GameCatalogResponse> result = gameService.getCatalogWithTrouble();

        assertEquals(1, result.size());
        assertEquals(4L, result.get(0).getId());
    }
}
