package com.example.videogamesshop.dto.game;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "Request body for partially updating a game")
public class GameUpdateRequest {
    @Schema(description = "Game title", example = "The Witcher 3: Wild Hunt")
    @Pattern(regexp = ".*\\S.*", message = "Title must not be blank")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @Schema(description = "Game price", example = "39.99")
    @DecimalMin(value = "0.0", inclusive = true,
            message = "Price must be greater than or equal to 0")
    private Double price;

    @Schema(description = "Release date", example = "2015-05-19")
    private LocalDate releaseDate;

    @Schema(description = "Game description", example = "Updated game description")
    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @ArraySchema(arraySchema = @Schema(description = "Category identifiers"))
    private List<@Positive(message = "Category ID must be positive") Long> categoryIds;
}
