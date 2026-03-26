package com.example.videogamesshop.exception;

public final class ApiErrorCode {

    public static final String NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String REQUEST_ERROR = "REQUEST_ERROR";
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    private ApiErrorCode() {
        throw new UnsupportedOperationException("Utility class");
    }
}
