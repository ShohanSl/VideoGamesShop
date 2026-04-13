package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.videogamesshop.cache.GameCacheService;
import com.example.videogamesshop.dto.publisher.PublisherCreateRequest;
import com.example.videogamesshop.dto.publisher.PublisherFullResponse;
import com.example.videogamesshop.entity.Publisher;
import com.example.videogamesshop.exception.PublisherNotFoundException;
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
class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameCacheService cacheService;

    @InjectMocks
    private PublisherService publisherService;

    @Test
    void shouldReturnAllPublishers() {
        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("EA");
        when(publisherRepository.findAll()).thenReturn(List.of(publisher));

        List<PublisherFullResponse> result = publisherService.getAllPublishers();

        assertEquals(1, result.size());
        assertEquals("EA", result.get(0).getName());
    }

    @Test
    void shouldReturnPublisherById() {
        Publisher publisher = new Publisher();
        publisher.setId(11L);
        publisher.setName("Sega");
        when(publisherRepository.findById(11L)).thenReturn(Optional.of(publisher));

        PublisherFullResponse result = publisherService.getPublisherById(11L);

        assertEquals(11L, result.getId());
        assertEquals("Sega", result.getName());
    }

    @Test
    void shouldThrowWhenPublisherNotFoundById() {
        when(publisherRepository.findById(12L)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> publisherService.getPublisherById(12L));
    }

    @Test
    void shouldCreatePublisher() {
        PublisherCreateRequest request = new PublisherCreateRequest();
        request.setName("Bethesda");
        request.setCountry("USA");
        request.setFoundedDate(LocalDate.of(1986, 6, 28));

        Publisher saved = new Publisher();
        saved.setId(1L);
        saved.setName("Bethesda");
        saved.setCountry("USA");
        saved.setFoundedDate(LocalDate.of(1986, 6, 28));

        when(publisherRepository.save(any(Publisher.class))).thenReturn(saved);

        PublisherFullResponse result = publisherService.createPublisher(request);

        assertEquals(1L, result.getId());
        assertEquals("Bethesda", result.getName());
    }

    @Test
    void shouldUpdatePublisher() {
        Publisher publisher = new Publisher();
        publisher.setId(1L);
        publisher.setName("Old");

        PublisherCreateRequest request = new PublisherCreateRequest();
        request.setName("New Name");
        request.setCountry("USA");
        request.setFoundedDate(LocalDate.of(2000, 1, 1));

        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));

        PublisherFullResponse result = publisherService.updatePublisher(1L, request);

        assertEquals("New Name", result.getName());
        assertEquals("USA", result.getCountry());
    }

    @Test
    void shouldThrowWhenUpdatingMissingPublisher() {
        PublisherCreateRequest request = new PublisherCreateRequest();
        request.setName("Missing");
        when(publisherRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class,
                () -> publisherService.updatePublisher(100L, request));
    }

    @Test
    void shouldThrowWhenDeletingMissingPublisher() {
        when(publisherRepository.existsById(10L)).thenReturn(false);

        assertThrows(PublisherNotFoundException.class,
                () -> publisherService.deletePublisher(10L));
    }

    @Test
    void shouldDeletePublisherWithGames() {
        when(publisherRepository.existsById(2L)).thenReturn(true);

        publisherService.deletePublisher(2L);

        verify(gameRepository).deleteUserLinksByPublisherId(2L);
        verify(gameRepository).deleteCategoryLinksByPublisherId(2L);
        verify(gameRepository).deleteGamesByPublisherId(2L);
        verify(publisherRepository).deleteById(2L);
        verify(cacheService).clear();
    }
}
