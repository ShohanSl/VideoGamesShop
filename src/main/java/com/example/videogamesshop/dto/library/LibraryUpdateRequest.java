package com.example.videogamesshop.dto.library;

import lombok.Data;

@Data
public class LibraryUpdateRequest {
    private String username; // если null, не обновляется
}