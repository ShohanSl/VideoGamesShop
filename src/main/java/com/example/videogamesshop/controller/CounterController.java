package com.example.videogamesshop.controller;

import com.example.videogamesshop.dto.concurrency.CounterResponse;
import com.example.videogamesshop.service.ThreadSafeCounterService;
import com.example.videogamesshop.service.ThreadSafeCounterService.CounterScenario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counter")
@RequiredArgsConstructor
@Tag(name = "Counter", description = "Counter scenarios for a prototype service with nested static classes")
public class CounterController {

    private final ObjectProvider<ThreadSafeCounterService> counterServiceProvider;

    @PostMapping("/{scenario}/increment")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Increment counter for selected scenario")
    public CounterResponse increment(@PathVariable CounterScenario scenario) {
        return new CounterResponse(counterServiceProvider.getObject().incrementAndGet(scenario));
    }

    @GetMapping("/{scenario}")
    @Operation(summary = "Get current counter value for selected scenario")
    public CounterResponse getCurrent(@PathVariable CounterScenario scenario) {
        return new CounterResponse(counterServiceProvider.getObject().getCurrent(scenario));
    }

    @PostMapping("/{scenario}/reset")
    @Operation(summary = "Reset counter for selected scenario")
    public CounterResponse reset(@PathVariable CounterScenario scenario) {
        return new CounterResponse(counterServiceProvider.getObject().reset(scenario));
    }
}
