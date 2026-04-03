package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.videogamesshop.cache.GameCacheService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private DeveloperMapper developerMapper;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private GameCacheService cacheService;

    @InjectMocks
    private DeveloperService developerService;

    @Test
    void shouldReturnAllDevelopers() {
        Developer developer = new Developer();
        developer.setId(1L);
        DeveloperCatalogResponse response = new DeveloperCatalogResponse();
        response.setId(1L);
        response.setName("Valve");
        when(developerRepository.findAll()).thenReturn(List.of(developer));
        when(developerMapper.toCatalogResponse(developer)).thenReturn(response);

        List<DeveloperCatalogResponse> result = developerService.getAllDevelopers();

        assertEquals(1, result.size());
        assertEquals("Valve", result.get(0).getName());
    }

    @Test
    void shouldReturnDeveloperById() {
        Developer developer = new Developer();
        developer.setId(2L);
        DeveloperFullResponse response = new DeveloperFullResponse();
        response.setId(2L);
        when(developerRepository.findById(2L)).thenReturn(Optional.of(developer));
        when(developerMapper.toFullResponse(developer)).thenReturn(response);

        DeveloperFullResponse result = developerService.getDeveloperById(2L);

        assertEquals(2L, result.getId());
    }

    @Test
    void shouldThrowWhenDeveloperMissingById() {
        when(developerRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(DeveloperNotFoundException.class,
                () -> developerService.getDeveloperById(9L));
    }

    @Test
    void shouldCreateDeveloper() {
        DeveloperCreateRequest request = new DeveloperCreateRequest();
        request.setName("BioWare");
        request.setCountry("Canada");
        request.setFoundedDate(LocalDate.of(1995, 2, 1));
        Developer saved = new Developer();
        saved.setId(3L);
        DeveloperFullResponse response = new DeveloperFullResponse();
        response.setId(3L);
        response.setName("BioWare");
        when(developerRepository.save(any(Developer.class))).thenReturn(saved);
        when(developerMapper.toFullResponse(saved)).thenReturn(response);

        DeveloperFullResponse result = developerService.createDeveloper(request);

        assertEquals(3L, result.getId());
        assertEquals("BioWare", result.getName());
    }

    @Test
    void shouldUpdateDeveloper() {
        Developer developer = new Developer();
        developer.setId(4L);
        DeveloperUpdateRequest request = new DeveloperUpdateRequest();
        request.setName("Updated Dev");
        DeveloperFullResponse response = new DeveloperFullResponse();
        response.setId(4L);
        response.setName("Updated Dev");
        when(developerRepository.findById(4L)).thenReturn(Optional.of(developer));
        when(developerMapper.toFullResponse(developer)).thenReturn(response);

        DeveloperFullResponse result = developerService.updateDeveloper(4L, request);

        verify(developerMapper).updateEntity(developer, request);
        assertEquals("Updated Dev", result.getName());
    }

    @Test
    void shouldThrowWhenUpdatingMissingDeveloper() {
        DeveloperUpdateRequest request = new DeveloperUpdateRequest();
        request.setName("Ghost");
        when(developerRepository.findById(44L)).thenReturn(Optional.empty());

        assertThrows(DeveloperNotFoundException.class,
                () -> developerService.updateDeveloper(44L, request));
    }

    @Test
    void shouldDeleteDeveloper() {
        when(developerRepository.existsById(5L)).thenReturn(true);

        developerService.deleteDeveloper(5L);

        verify(developerRepository).deleteById(5L);
        verify(cacheService).clear();
    }

    @Test
    void shouldThrowWhenDeletingMissingDeveloper() {
        when(developerRepository.existsById(6L)).thenReturn(false);

        assertThrows(DeveloperNotFoundException.class,
                () -> developerService.deleteDeveloper(6L));
    }

    @Test
    void shouldCreateDeveloperWithGames() {
        DeveloperCreateRequest developerRequest = new DeveloperCreateRequest();
        developerRequest.setName("CD Projekt Red");
        developerRequest.setCountry("Poland");
        developerRequest.setFoundedDate(LocalDate.of(1994, 5, 1));

        DeveloperBulkGameRequest gameRequest = new DeveloperBulkGameRequest();
        gameRequest.setTitle("The Witcher 3");
        gameRequest.setPrice(49.99);
        gameRequest.setReleaseDate(LocalDate.of(2015, 5, 19));
        gameRequest.setDescription("RPG");
        gameRequest.setPublisherId(2L);
        gameRequest.setCategoryIds(List.of(3L));

        Developer savedDeveloper = new Developer();
        savedDeveloper.setId(1L);
        savedDeveloper.setName("CD Projekt Red");
        savedDeveloper.setCountry("Poland");
        savedDeveloper.setFoundedDate(LocalDate.of(1994, 5, 1));

        Publisher publisher = new Publisher();
        publisher.setId(2L);
        Category category = new Category();
        category.setId(3L);

        GameFullResponse gameResponse = new GameFullResponse();
        gameResponse.setId(10L);
        gameResponse.setTitle("The Witcher 3");
        DeveloperFullResponse developerResponse = new DeveloperFullResponse();
        developerResponse.setId(1L);
        developerResponse.setName("CD Projekt Red");

        when(developerRepository.save(any(Developer.class))).thenReturn(savedDeveloper);
        when(publisherRepository.findById(2L)).thenReturn(Optional.of(publisher));
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(gameMapper.toFullResponse(any(Game.class))).thenReturn(gameResponse);
        when(developerMapper.toFullResponse(savedDeveloper)).thenReturn(developerResponse);

        DeveloperWithGamesResponse result = developerService.createDeveloperWithGamesWithTransaction(
                developerRequest, List.of(gameRequest));

        assertSame(developerResponse, result.getDeveloper());
        assertEquals(1, result.getCreatedGamesCount());
        assertEquals("The Witcher 3", result.getGames().get(0).getTitle());
        verify(gameRepository).save(any(Game.class));
        verify(cacheService).clear();
    }

    @Test
    void shouldCreateDeveloperWithGamesWithoutTransaction() {
        DeveloperCreateRequest developerRequest = new DeveloperCreateRequest();
        developerRequest.setName("Remedy");
        developerRequest.setCountry("Finland");
        developerRequest.setFoundedDate(LocalDate.of(1995, 8, 18));

        DeveloperBulkGameRequest gameRequest = new DeveloperBulkGameRequest();
        gameRequest.setTitle("Control");
        gameRequest.setPrice(39.99);
        gameRequest.setReleaseDate(LocalDate.of(2019, 8, 27));
        gameRequest.setDescription("Action adventure");
        gameRequest.setPublisherId(5L);
        gameRequest.setCategoryIds(List.of(7L));

        Developer savedDeveloper = new Developer();
        savedDeveloper.setId(9L);
        Publisher publisher = new Publisher();
        publisher.setId(5L);
        Category category = new Category();
        category.setId(7L);

        DeveloperFullResponse developerResponse = new DeveloperFullResponse();
        developerResponse.setId(9L);
        developerResponse.setName("Remedy");

        GameFullResponse gameResponse = new GameFullResponse();
        gameResponse.setId(15L);
        gameResponse.setTitle("Control");

        when(developerRepository.save(any(Developer.class))).thenReturn(savedDeveloper);
        when(publisherRepository.findById(5L)).thenReturn(Optional.of(publisher));
        when(categoryRepository.findById(7L)).thenReturn(Optional.of(category));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(gameMapper.toFullResponse(any(Game.class))).thenReturn(gameResponse);
        when(developerMapper.toFullResponse(savedDeveloper)).thenReturn(developerResponse);

        DeveloperWithGamesResponse result = developerService.createDeveloperWithGamesWithoutTransaction(
                developerRequest, List.of(gameRequest));

        assertSame(developerResponse, result.getDeveloper());
        assertEquals(1, result.getCreatedGamesCount());
        assertEquals("Control", result.getGames().get(0).getTitle());
        verify(cacheService).clear();
    }

    @Test
    void shouldClearCacheWhenWithoutTransactionBulkOperationFailsAfterPartialSave() {
        DeveloperCreateRequest developerRequest = new DeveloperCreateRequest();
        developerRequest.setName("Test Transaction Studio");
        developerRequest.setCountry("Poland");
        developerRequest.setFoundedDate(LocalDate.of(2020, 1, 10));

        DeveloperBulkGameRequest gameRequest = new DeveloperBulkGameRequest();
        gameRequest.setTitle("Broken Game");
        gameRequest.setPrice(39.99);
        gameRequest.setReleaseDate(LocalDate.of(2024, 2, 1));
        gameRequest.setDescription("This one should fail");
        gameRequest.setPublisherId(999L);
        gameRequest.setCategoryIds(List.of(1L));

        Developer savedDeveloper = new Developer();
        savedDeveloper.setId(100L);

        when(developerRepository.save(any(Developer.class))).thenReturn(savedDeveloper);
        when(publisherRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> developerService.createDeveloperWithGamesWithoutTransaction(
                        developerRequest, List.of(gameRequest)));

        verify(cacheService).clear();
    }

    @Test
    void shouldNotClearCacheWhenWithTransactionBulkOperationFails() {
        DeveloperCreateRequest developerRequest = new DeveloperCreateRequest();
        developerRequest.setName("Tx Studio");
        developerRequest.setCountry("Poland");
        developerRequest.setFoundedDate(LocalDate.of(2020, 1, 10));

        DeveloperBulkGameRequest gameRequest = new DeveloperBulkGameRequest();
        gameRequest.setTitle("Broken Game");
        gameRequest.setPrice(39.99);
        gameRequest.setReleaseDate(LocalDate.of(2024, 2, 1));
        gameRequest.setDescription("This one should fail");
        gameRequest.setPublisherId(999L);
        gameRequest.setCategoryIds(List.of(1L));

        Developer savedDeveloper = new Developer();
        savedDeveloper.setId(101L);

        when(developerRepository.save(any(Developer.class))).thenReturn(savedDeveloper);
        when(publisherRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> developerService.createDeveloperWithGamesWithTransaction(
                        developerRequest, List.of(gameRequest)));

        verify(cacheService, never()).clear();
    }

    @Test
    void shouldNotClearCacheWhenWithoutTransactionFailsBeforeAnySave() {
        DeveloperCreateRequest developerRequest = new DeveloperCreateRequest();
        developerRequest.setName("Early Failure Studio");
        developerRequest.setCountry("Poland");
        developerRequest.setFoundedDate(LocalDate.of(2020, 1, 10));

        when(developerRepository.save(any(Developer.class))).thenThrow(new RuntimeException("db down"));

        assertThrows(RuntimeException.class,
                () -> developerService.createDeveloperWithGamesWithoutTransaction(
                        developerRequest, List.of(new DeveloperBulkGameRequest())));

        verify(cacheService, never()).clear();
    }

    @Test
    void shouldThrowWhenPublisherMissingInBulkOperation() {
        DeveloperCreateRequest developerRequest = new DeveloperCreateRequest();
        developerRequest.setName("Valve");
        developerRequest.setCountry("USA");
        developerRequest.setFoundedDate(LocalDate.of(1996, 8, 24));

        DeveloperBulkGameRequest gameRequest = new DeveloperBulkGameRequest();
        gameRequest.setTitle("Half-Life");
        gameRequest.setPrice(10.0);
        gameRequest.setReleaseDate(LocalDate.of(1998, 11, 19));
        gameRequest.setDescription("FPS");
        gameRequest.setPublisherId(99L);
        gameRequest.setCategoryIds(List.of(1L));

        Developer savedDeveloper = new Developer();
        savedDeveloper.setId(1L);
        List<DeveloperBulkGameRequest> gameRequests = List.of(gameRequest);
        when(developerRepository.save(any(Developer.class))).thenReturn(savedDeveloper);
        when(publisherRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> developerService.createDeveloperWithGamesWithoutTransaction(
                        developerRequest, gameRequests));
        verify(cacheService).clear();
    }

    @Test
    void shouldThrowWhenCategoryMissingInBulkOperation() {
        DeveloperCreateRequest developerRequest = new DeveloperCreateRequest();
        developerRequest.setName("Valve");
        developerRequest.setCountry("USA");
        developerRequest.setFoundedDate(LocalDate.of(1996, 8, 24));

        DeveloperBulkGameRequest gameRequest = new DeveloperBulkGameRequest();
        gameRequest.setTitle("Portal");
        gameRequest.setPrice(9.99);
        gameRequest.setReleaseDate(LocalDate.of(2007, 10, 10));
        gameRequest.setDescription("Puzzle");
        gameRequest.setPublisherId(2L);
        gameRequest.setCategoryIds(List.of(404L));

        Developer savedDeveloper = new Developer();
        savedDeveloper.setId(1L);
        Publisher publisher = new Publisher();
        publisher.setId(2L);

        when(developerRepository.save(any(Developer.class))).thenReturn(savedDeveloper);
        when(publisherRepository.findById(2L)).thenReturn(Optional.of(publisher));
        when(categoryRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> developerService.createDeveloperWithGamesWithTransaction(
                        developerRequest, List.of(gameRequest)));
    }

    @Test
    void shouldAttachGameToDeveloper() {
        Developer developer = new Developer();
        developer.setId(1L);
        Game game = new Game();
        game.setId(2L);
        when(developerRepository.findById(1L)).thenReturn(Optional.of(developer));
        when(gameRepository.findById(2L)).thenReturn(Optional.of(game));

        developerService.addGameToDeveloper(1L, 2L);

        assertEquals(1, developer.getGames().size());
        assertSame(developer, game.getDeveloper());
        verify(cacheService).clear();
    }

    @Test
    void shouldThrowWhenAttachingMissingGameToDeveloper() {
        Developer developer = new Developer();
        developer.setId(1L);
        when(developerRepository.findById(1L)).thenReturn(Optional.of(developer));
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class,
                () -> developerService.addGameToDeveloper(1L, 99L));
    }

    @Test
    void shouldRemoveGameFromDeveloper() {
        Developer developer = new Developer();
        developer.setId(7L);
        Game game = new Game();
        game.setId(8L);
        developer.addGame(game);
        when(developerRepository.findById(7L)).thenReturn(Optional.of(developer));
        when(gameRepository.findById(8L)).thenReturn(Optional.of(game));

        developerService.removeGameFromDeveloper(7L, 8L);

        assertEquals(0, developer.getGames().size());
        assertNull(game.getDeveloper());
        verify(cacheService).clear();
    }
}
