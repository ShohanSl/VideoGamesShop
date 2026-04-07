package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.async.AsyncJobStartResponse;
import com.example.videogamesshop.dto.async.AsyncJobStatus;
import com.example.videogamesshop.dto.async.AsyncJobStatusResponse;
import com.example.videogamesshop.service.AsyncJobRegistryService;
import com.example.videogamesshop.service.AsyncReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/async-jobs")
@RequiredArgsConstructor
@Validated
@Tag(name = "Async jobs", description = "Async business operations with task tracking")
public class AsyncJobController {

    private final AsyncReportService asyncReportService;
    private final AsyncJobRegistryService asyncJobRegistryService;

    @PostMapping("/catalog-report")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Start async catalog report generation")
    public AsyncJobStartResponse startCatalogReport() {
        String taskId = asyncReportService.startCatalogReportJob();
        return new AsyncJobStartResponse(taskId, AsyncJobStatus.PENDING);
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Get async job status by task id")
    public AsyncJobStatusResponse getJobStatus(
            @PathVariable @NotBlank(message = "Task id is required") String taskId) {
        return asyncJobRegistryService.getJobStatus(taskId);
    }
}
