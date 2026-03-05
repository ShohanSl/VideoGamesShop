package com.example.videogamesshop.dto.publisher;

import com.example.videogamesshop.dto.game.GameSimpleDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class PublisherFullResponse {
    private Long id;
    private String name;
    private String country;
    private LocalDate foundedDate;
    private List<GameSimpleDto> games;
}