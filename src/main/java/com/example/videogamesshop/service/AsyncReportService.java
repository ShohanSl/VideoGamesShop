package com.example.videogamesshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncReportService {

    private final AsyncJobRegistryService asyncJobRegistryService;
    private final AsyncCatalogReportWorker asyncCatalogReportWorker;

    public String startCatalogReportJob() {
        AsyncJobState state = asyncJobRegistryService.createJob();
        asyncCatalogReportWorker.buildCatalogReport(state.getTaskId());
        return state.getTaskId();
    }
}
