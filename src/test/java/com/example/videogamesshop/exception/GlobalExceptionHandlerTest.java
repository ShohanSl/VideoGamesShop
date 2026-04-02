package com.example.videogamesshop.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.example.videogamesshop.dto.error.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldReturnConflictResponseForDataIntegrityViolation() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        DataIntegrityViolationException exception = new DataIntegrityViolationException(
                "could not execute statement",
                new RuntimeException("duplicate key value violates unique constraint \"users_username_key\"")
        );

        ResponseEntity<ApiErrorResponse> response = handler.handleConflictException(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(ApiErrorCode.CONFLICT_ERROR, response.getBody().code());
        assertEquals("Request conflicts with the current state of the resource",
                response.getBody().message());
        assertFalse(response.getBody().details().isEmpty());
        assertEquals("duplicate key value violates unique constraint \"users_username_key\"",
                response.getBody().details().get(0).message());
    }
}
