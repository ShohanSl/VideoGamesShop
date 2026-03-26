package com.example.videogamesshop.dto.user;

import com.example.videogamesshop.dto.game.GameSimpleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "Detailed user response")
public class UserFullResponse {
    private Long id;
    private String username;
    private List<GameSimpleDto> games;
}
