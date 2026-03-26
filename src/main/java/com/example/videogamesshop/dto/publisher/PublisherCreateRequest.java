package com.example.videogamesshop.dto.publisher;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "Request body for creating or updating a publisher")
public class PublisherCreateRequest {
    @Schema(description = "Publisher name", example = "Bethesda Softworks")
    @NotBlank(message = "Publisher name is required")
    @Size(max = 255, message = "Publisher name must be at most 255 characters")
    private String name;

    @Schema(description = "Publisher country", example = "United States")
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must be at most 100 characters")
    private String country;

    @Schema(description = "Publisher foundation date", example = "1986-06-28")
    @NotNull(message = "Founded date is required")
    @PastOrPresent(message = "Founded date must be in the past or present")
    private LocalDate foundedDate;
}
