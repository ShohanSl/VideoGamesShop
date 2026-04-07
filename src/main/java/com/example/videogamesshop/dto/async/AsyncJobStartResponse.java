package com.example.videogamesshop.dto.async;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response with created async job id")
public record AsyncJobStartResponse(
        @Schema(description = "Async job id")
        String taskId,
        @Schema(description = "Initial status", example = "PENDING")
        AsyncJobStatus status
) {
}
