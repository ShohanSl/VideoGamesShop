package com.example.videogamesshop.exception;

import com.example.videogamesshop.dto.error.ApiErrorResponse;
import com.example.videogamesshop.dto.error.ApiValidationError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String REQUEST_VALIDATION_FAILED = "Request validation failed";
    private static final String DATA_CONFLICT_MESSAGE =
            "Request conflicts with the current state of the resource";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = "id".equals(ex.getField())
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;
        String code = "id".equals(ex.getField())
                ? ApiErrorCode.NOT_FOUND
                : ApiErrorCode.VALIDATION_ERROR;
        String message = "id".equals(ex.getField())
                ? ex.getMessage()
                : REQUEST_VALIDATION_FAILED;

        return buildResponse(
                status,
                code,
                message,
                request.getRequestURI(),
                List.of(new ApiValidationError(ex.getField(), ex.getMessage()))
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<ApiValidationError> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .sorted(Comparator.comparing(FieldError::getField))
                .map(error -> new ApiValidationError(error.getField(), error.getDefaultMessage()))
                .toList();

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ApiErrorCode.VALIDATION_ERROR,
                REQUEST_VALIDATION_FAILED,
                request.getRequestURI(),
                details
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpServletRequest request
    ) {
        List<ApiValidationError> details = ex.getParameterValidationResults()
                .stream()
                .flatMap(result -> result.getResolvableErrors().stream()
                        .map(error -> new ApiValidationError(
                                result.getMethodParameter().getParameterName(),
                                error.getDefaultMessage())))
                .toList();

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ApiErrorCode.VALIDATION_ERROR,
                REQUEST_VALIDATION_FAILED,
                request.getRequestURI(),
                details
        );
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ApiErrorCode.REQUEST_ERROR,
                ex.getMessage(),
                request.getRequestURI(),
                List.of(new ApiValidationError("request", ex.getMessage()))
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConflictException(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        String detailMessage = extractMostSpecificMessage(ex);

        return buildResponse(
                HttpStatus.CONFLICT,
                ApiErrorCode.CONFLICT_ERROR,
                DATA_CONFLICT_MESSAGE,
                request.getRequestURI(),
                List.of(new ApiValidationError("request", detailMessage))
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                ApiErrorCode.REQUEST_ERROR,
                "Invalid credentials",
                request.getRequestURI(),
                List.of()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnhandledException(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Unhandled exception while processing request [{} {}]",
                request.getMethod(),
                request.getRequestURI(),
                ex);

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ApiErrorCode.INTERNAL_ERROR,
                "Unexpected server error",
                request.getRequestURI(),
                List.of()
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String code,
            String message,
            String path,
            List<ApiValidationError> details
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                code,
                message,
                path,
                details
        );
        return ResponseEntity.status(status).body(response);
    }

    private String extractMostSpecificMessage(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        if (StringUtils.hasText(cause.getMessage())) {
            return cause.getMessage();
        }
        if (StringUtils.hasText(ex.getMessage())) {
            return ex.getMessage();
        }
        return DATA_CONFLICT_MESSAGE;
    }
}
