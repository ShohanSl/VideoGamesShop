package com.example.videogamesshop.dto.developer;

import com.example.videogamesshop.dto.game.GameSimpleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;


@Data
@Schema(description = "Detailed developer response")
public class DeveloperFullResponse {
    private Long id;
    private String name;
    private String country;
    private LocalDate foundedDate;
    private List<GameSimpleDto> games;
}
