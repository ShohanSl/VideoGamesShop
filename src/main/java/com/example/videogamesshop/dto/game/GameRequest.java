package com.example.videogamesshop.dto.game;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "Request body for creating a game")
public class GameRequest {
    @Schema(description = "Game title", example = "The Witcher 3")
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @Schema(description = "Game price", example = "49.99")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    private Double price;

    @Schema(description = "Release date", example = "2015-05-19")
    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

    @Schema(description = "Game description", example = "Story-driven open world RPG")
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @Schema(description = "Developer identifier", example = "1")
    @NotNull(message = "Developer ID is required")
    @Positive(message = "Developer ID must be positive")
    private Long developerId;

    @Schema(description = "Publisher identifier", example = "2")
    @NotNull(message = "Publisher ID is required")
    @Positive(message = "Publisher ID must be positive")
    private Long publisherId;

    @ArraySchema(arraySchema = @Schema(description = "Category identifiers"))
    @NotEmpty(message = "At least one category ID is required")
    private List<@NotNull(message = "Category ID must not be null")
            @Positive(message = "Category ID must be positive") Long> categoryIds;
}
