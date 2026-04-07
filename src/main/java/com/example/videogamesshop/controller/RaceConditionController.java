package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.concurrency.RaceConditionRequest;
import com.example.videogamesshop.dto.concurrency.RaceConditionResponse;
import com.example.videogamesshop.service.RaceConditionDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concurrency/race-condition")
@RequiredArgsConstructor
@Tag(name = "Concurrency", description = "Race condition demonstration endpoints")
public class RaceConditionController {

    private final RaceConditionDemoService raceConditionDemoService;

    @PostMapping("/unsafe")
    @Operation(summary = "Run unsafe counter demo and observe race condition")
    public RaceConditionResponse runUnsafe(@Valid @RequestBody RaceConditionRequest request) {
        return raceConditionDemoService.runUnsafeDemo(
                request.threads(),
                request.incrementsPerThread()
        );
    }

    @PostMapping("/safe")
    @Operation(summary = "Run safe counter demo with AtomicLong")
    public RaceConditionResponse runSafe(@Valid @RequestBody RaceConditionRequest request) {
        return raceConditionDemoService.runSafeDemo(
                request.threads(),
                request.incrementsPerThread()
        );
    }
}
