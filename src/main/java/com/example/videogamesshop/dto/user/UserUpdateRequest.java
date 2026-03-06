package com.example.videogamesshop.dto.user;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String username; // если null, не обновляется
}