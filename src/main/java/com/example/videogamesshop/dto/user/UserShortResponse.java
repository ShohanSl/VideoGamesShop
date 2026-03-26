package com.example.videogamesshop.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Compact user response")
public class UserShortResponse {
    private Long id;
    private String username;
}
