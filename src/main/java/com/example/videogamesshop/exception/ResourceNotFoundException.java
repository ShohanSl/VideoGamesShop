package com.example.videogamesshop.exception;

public abstract class ResourceNotFoundException extends RuntimeException {

    private final String field;
    private final transient Object rejectedValue;

    protected ResourceNotFoundException(String resourceName, Long id) {
        this(resourceName, "id", id);
    }

    protected ResourceNotFoundException(String resourceName, String field, Object rejectedValue) {
        super(resourceName + " not found for " + field + ": " + rejectedValue);
        this.field = field;
        this.rejectedValue = rejectedValue;
    }

    public String getField() {
        return field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}
