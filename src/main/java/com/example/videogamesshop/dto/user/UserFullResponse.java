package com.example.videogamesshop.dto.user;

import com.example.videogamesshop.dto.game.GameSimpleDto;
import java.util.List;
import lombok.Data;

@Data
public class UserFullResponse {
    private Long id;
    private String username;
    private List<GameSimpleDto> games;
}