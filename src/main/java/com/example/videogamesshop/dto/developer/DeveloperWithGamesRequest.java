package com.example.videogamesshop.dto.developer;

import com.example.videogamesshop.dto.game.GameRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "Composite request for creating a developer together with games")
public class DeveloperWithGamesRequest {
    @Schema(description = "Developer data")
    @Valid
    @NotNull(message = "Developer payload is required")
    private DeveloperCreateRequest developer;

    @Schema(description = "Games to create for the developer")
    @Valid
    @NotEmpty(message = "At least one game is required")
    private List<GameRequest> games;
}
