package com.example.videogamesshop.dto.developer;

import com.example.videogamesshop.dto.game.GameFullResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "Response for bulk creation of developer with games")
public class DeveloperWithGamesResponse {
    @Schema(description = "Created developer")
    private DeveloperFullResponse developer;

    @Schema(description = "Created games")
    private List<GameFullResponse> games;

    @Schema(description = "Number of created games", example = "2")
    private int createdGamesCount;
}
