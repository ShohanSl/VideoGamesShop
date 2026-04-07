package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.concurrency.CounterResponse;
import com.example.videogamesshop.service.ThreadSafeCounterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counter")
@RequiredArgsConstructor
@Tag(name = "Counter", description = "Thread-safe counter demo endpoints")
public class CounterController {

    private final ThreadSafeCounterService threadSafeCounterService;

    @PostMapping("/increment")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Increment thread-safe counter")
    public CounterResponse increment() {
        return new CounterResponse(threadSafeCounterService.incrementAndGet());
    }

    @GetMapping
    @Operation(summary = "Get current counter value")
    public CounterResponse getCurrent() {
        return new CounterResponse(threadSafeCounterService.getCurrent());
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset counter to zero")
    public CounterResponse reset() {
        return new CounterResponse(threadSafeCounterService.reset());
    }
}
