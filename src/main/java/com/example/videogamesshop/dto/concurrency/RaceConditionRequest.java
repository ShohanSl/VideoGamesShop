package com.example.videogamesshop.dto.concurrency;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "Parameters for race condition demo")
public record RaceConditionRequest(
        @Min(value = 50, message = "Threads count must be at least 50")
        @Schema(description = "Number of threads", example = "50")
        int threads,
        @Min(value = 1, message = "Increments per thread must be positive")
        @Schema(description = "How many increments each thread performs", example = "1000")
        int incrementsPerThread
) {
}
