package com.example.videogamesshop.dto.async;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;

@Schema(description = "Current async job state")
public record AsyncJobStatusResponse(
        @Schema(description = "Async job id")
        String taskId,
        @Schema(description = "Current status", example = "RUNNING")
        AsyncJobStatus status,
        @Schema(description = "Job creation time")
        OffsetDateTime createdAt,
        @Schema(description = "Job start time")
        OffsetDateTime startedAt,
        @Schema(description = "Job finish time")
        OffsetDateTime finishedAt,
        @Schema(description = "Result summary for completed job")
        String result,
        @Schema(description = "Error message for failed job")
        String error
) {
}
