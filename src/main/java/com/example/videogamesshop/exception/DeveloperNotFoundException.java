package com.example.videogamesshop.exception;

public class DeveloperNotFoundException extends RuntimeException {
    public DeveloperNotFoundException(Long id) {
        super("Developer not found with id: " + id);
    }
}