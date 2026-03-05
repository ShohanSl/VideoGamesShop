package com.example.videogamesshop.dto.developer;

import com.example.videogamesshop.dto.game.GameRequest;
import java.util.List;
import lombok.Data;

@Data
public class DeveloperWithGamesRequest {
    private DeveloperCreateRequest developer;
    private List<GameRequest> games;
}
