package com.example.videogamesshop.dto.developer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "Request body for partially updating a developer")
public class DeveloperUpdateRequest {
    @Schema(description = "Developer name", example = "CD Projekt RED")
    @Pattern(regexp = ".*\\S.*", message = "Name must not be blank")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String name;

    @Schema(description = "Developer country", example = "Poland")
    @Pattern(regexp = ".*\\S.*", message = "Country must not be blank")
    @Size(max = 100, message = "Country must be at most 100 characters")
    private String country;

    @Schema(description = "Developer foundation date", example = "1994-05-01")
    @PastOrPresent(message = "Founded date must be in the past or present")
    private LocalDate foundedDate;
}
