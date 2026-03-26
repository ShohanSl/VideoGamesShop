package com.example.videogamesshop.dto.error;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "Unified API error response")
public record ApiErrorResponse(
        @Schema(description = "Time when the error occurred",
                example = "2026-03-25T14:30:00+03:00")
        OffsetDateTime timestamp,
        @Schema(description = "HTTP status code", example = "400")
        int status,
        @Schema(description = "HTTP status reason", example = "Bad Request")
        String error,
        @Schema(description = "Application-specific error code", example = "VALIDATION_ERROR")
        String code,
        @Schema(description = "Human-readable error message",
                example = "Request validation failed")
        String message,
        @Schema(description = "Request path", example = "/games")
        String path,
        @ArraySchema(arraySchema = @Schema(description = "Field-level validation details"))
        List<ApiValidationError> details
) {
}
