package com.example.videogamesshop.dto.concurrency;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Thread-safe counter state")
public record CounterResponse(
        @Schema(description = "Current counter value", example = "42")
        long value
) {
}
