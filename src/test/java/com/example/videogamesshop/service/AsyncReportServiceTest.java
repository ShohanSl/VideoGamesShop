package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AsyncReportServiceTest {

    @Mock
    private AsyncJobRegistryService asyncJobRegistryService;

    @Mock
    private AsyncCatalogReportWorker asyncCatalogReportWorker;

    @InjectMocks
    private AsyncReportService asyncReportService;

    @Test
    void shouldCreateJobAndTriggerAsyncWorker() {
        AsyncJobState state = new AsyncJobState("task-1");
        when(asyncJobRegistryService.createJob()).thenReturn(state);

        String taskId = asyncReportService.startCatalogReportJob();

        assertEquals("task-1", taskId);
        verify(asyncJobRegistryService).createJob();
        verify(asyncCatalogReportWorker).buildCatalogReport("task-1");
    }
}
