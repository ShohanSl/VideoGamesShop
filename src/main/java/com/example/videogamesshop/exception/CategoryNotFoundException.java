package com.example.videogamesshop.exception;

public class CategoryNotFoundException extends ResourceNotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Category", id);
    }

    public CategoryNotFoundException(String field, Object rejectedValue) {
        super("Category", field, rejectedValue);
    }
}
