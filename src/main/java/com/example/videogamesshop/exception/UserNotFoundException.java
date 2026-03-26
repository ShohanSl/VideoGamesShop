package com.example.videogamesshop.exception;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(Long id) {
        super("User", id);
    }

    public UserNotFoundException(String field, Object rejectedValue) {
        super("User", field, rejectedValue);
    }
}
