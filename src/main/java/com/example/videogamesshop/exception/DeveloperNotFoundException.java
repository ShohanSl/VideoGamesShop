package com.example.videogamesshop.exception;

public class DeveloperNotFoundException extends ResourceNotFoundException {
    public DeveloperNotFoundException(Long id) {
        super("Developer", id);
    }

    public DeveloperNotFoundException(String field, Object rejectedValue) {
        super("Developer", field, rejectedValue);
    }
}
