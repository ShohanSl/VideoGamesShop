package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.videogamesshop.dto.async.AsyncJobStatus;
import com.example.videogamesshop.exception.AsyncJobNotFoundException;
import org.junit.jupiter.api.Test;

class AsyncJobRegistryServiceTest {

    private final AsyncJobRegistryService asyncJobRegistryService = new AsyncJobRegistryService();

    @Test
    void shouldCreateJobWithPendingStatus() {
        AsyncJobState state = asyncJobRegistryService.createJob();

        assertNotNull(state.getTaskId());
        assertEquals(AsyncJobStatus.PENDING, state.getStatus());
        assertNotNull(state.getCreatedAt());
    }

    @Test
    void shouldReturnJobStatus() {
        AsyncJobState state = asyncJobRegistryService.createJob();
        state.markRunning();
        state.markCompleted("done");

        var response = asyncJobRegistryService.getJobStatus(state.getTaskId());

        assertEquals(state.getTaskId(), response.taskId());
        assertEquals(AsyncJobStatus.COMPLETED, response.status());
        assertEquals("done", response.result());
    }

    @Test
    void shouldThrowWhenJobMissing() {
        assertThrows(AsyncJobNotFoundException.class,
                () -> asyncJobRegistryService.getJobStatus("missing"));
    }
}
