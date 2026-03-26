package com.example.videogamesshop.exception;

public class GameNotFoundException extends ResourceNotFoundException {
    public GameNotFoundException(Long id) {
        super("Game", id);
    }

    public GameNotFoundException(String field, Object rejectedValue) {
        super("Game", field, rejectedValue);
    }
}
