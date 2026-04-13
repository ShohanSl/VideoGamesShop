package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.videogamesshop.dto.async.AsyncJobStatus;
import com.example.videogamesshop.repository.CategoryRepository;
import com.example.videogamesshop.repository.DeveloperRepository;
import com.example.videogamesshop.repository.GameRepository;
import com.example.videogamesshop.repository.PublisherRepository;
import com.example.videogamesshop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AsyncCatalogReportWorkerTest {

    @Mock
    private AsyncJobRegistryService asyncJobRegistryService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AsyncCatalogReportWorker asyncCatalogReportWorker;

    @Test
    void shouldCompleteJobWhenReportBuilt() {
        ReflectionTestUtils.setField(asyncCatalogReportWorker, "runningDelayMs", 0L);
        AsyncJobState state = new AsyncJobState("task-1");
        when(asyncJobRegistryService.getJobState("task-1")).thenReturn(state);
        when(gameRepository.count()).thenReturn(10L);
        when(developerRepository.count()).thenReturn(2L);
        when(publisherRepository.count()).thenReturn(3L);
        when(categoryRepository.count()).thenReturn(4L);
        when(userRepository.count()).thenReturn(5L);

        asyncCatalogReportWorker.buildCatalogReport("task-1");

        assertEquals(AsyncJobStatus.COMPLETED, state.getStatus());
        assertEquals(
                "Catalog report completed: games=10, developers=2, publishers=3, categories=4, users=5",
                state.getResult()
        );
    }

    @Test
    void shouldFailJobWhenRepositoryThrows() {
        ReflectionTestUtils.setField(asyncCatalogReportWorker, "runningDelayMs", 0L);
        AsyncJobState state = new AsyncJobState("task-2");
        when(asyncJobRegistryService.getJobState("task-2")).thenReturn(state);
        when(gameRepository.count()).thenThrow(new IllegalStateException("db down"));

        assertThrows(IllegalStateException.class,
                () -> asyncCatalogReportWorker.buildCatalogReport("task-2"));

        assertEquals(AsyncJobStatus.FAILED, state.getStatus());
        assertEquals("db down", state.getError());
    }
}
