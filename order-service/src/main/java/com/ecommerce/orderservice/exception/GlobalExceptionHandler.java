package com.ecommerce.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponseError error = new ApiResponseError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseError> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ApiResponseError error = new ApiResponseError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errorMessage
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiResponseError> handleServiceUnavailableException(ServiceUnavailableException ex) {
        ApiResponseError error = new ApiResponseError(
                LocalDateTime.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseError> handleGlobalException(Exception ex) {
        ApiResponseError error = new ApiResponseError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage() != null ? ex.getMessage() : "Internal Server Error"
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
