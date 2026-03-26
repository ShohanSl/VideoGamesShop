package com.example.videogamesshop.dto.publisher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Compact publisher response")
public class PublisherDto {
    private Long id;
    private String name;
}
