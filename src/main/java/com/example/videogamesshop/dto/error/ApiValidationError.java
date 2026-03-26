package com.example.videogamesshop.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Validation error for a specific request field or parameter")
public record ApiValidationError(
        @Schema(description = "Field or parameter name", example = "title")
        String field,
        @Schema(description = "Validation message", example = "Title is required")
        String message
) {
}
