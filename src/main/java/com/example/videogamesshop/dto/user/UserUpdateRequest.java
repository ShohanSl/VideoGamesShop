package com.example.videogamesshop.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request body for partially updating a user")
public class UserUpdateRequest {
    @Schema(description = "Unique username", example = "player_two")
    @Pattern(regexp = ".*\\S.*", message = "Username must not be blank")
    @Size(max = 100, message = "Username must be at most 100 characters")
    private String username;

    @Schema(description = "New password", example = "StrongPass123!")
    @Pattern(regexp = "^$|.*\\S.*", message = "Password must not be blank")
    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
    private String password;
}
