package com.example.videogamesshop.exception;

public class PublisherNotFoundException extends ResourceNotFoundException {
    public PublisherNotFoundException(Long id) {
        super("Publisher", id);
    }

    public PublisherNotFoundException(String field, Object rejectedValue) {
        super("Publisher", field, rejectedValue);
    }
}
