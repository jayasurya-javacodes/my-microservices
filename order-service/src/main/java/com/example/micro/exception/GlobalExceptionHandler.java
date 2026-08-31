package com.example.micro.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserServiceUnavailableException.class)
    public ResponseEntity<ApiError> handleUserServiceUnavailable(
            UserServiceUnavailableException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.SERVICE_UNAVAILABLE,
                "USER_SERVICE_UNAVAILABLE",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(PaymentServiceUnavailableException.class)
    public ResponseEntity<ApiError> handlePaymentServiceUnavailable(
            PaymentServiceUnavailableException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.SERVICE_UNAVAILABLE,
                "PAYMENT_SERVICE_UNAVAILABLE",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiError> handleOrderNotFound(
            OrderNotFoundException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.NOT_FOUND,
                "ORDER_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage()
                )
                .orElse("Invalid request");

        return buildError(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                message,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(
            Exception ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                request.getRequestURI()
        );
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ApiError> handleAccessDenied(
//            AccessDeniedException ex,
//            HttpServletRequest request) {
//
//        ApiError apiError = new ApiError(
//                LocalDateTime.now(),
//                HttpStatus.FORBIDDEN.value(),
//                HttpStatus.FORBIDDEN.getReasonPhrase(),
//                "Access Denied",
//                request.getRequestURI()
//        );

//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(apiError);
//    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(
            InvalidCredentialsException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.UNAUTHORIZED,
                "UNAUTHORIZED",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String error,
            String message,
            String path) {

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                path
        );

        return ResponseEntity
                .status(status)
                .body(apiError);
    }
}