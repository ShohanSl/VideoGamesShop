package com.example.videogamesshop.dto.concurrency;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Result of concurrent increment demo")
public record RaceConditionResponse(
        @Schema(description = "Demo mode", example = "unsafe")
        String mode,
        @Schema(description = "Number of threads")
        int threads,
        @Schema(description = "Increments per thread")
        int incrementsPerThread,
        @Schema(description = "Expected final value")
        long expected,
        @Schema(description = "Actual final value")
        long actual,
        @Schema(description = "Lost updates due to race condition")
        long lostUpdates,
        @Schema(description = "Execution time in milliseconds")
        long durationMs
) {
}
