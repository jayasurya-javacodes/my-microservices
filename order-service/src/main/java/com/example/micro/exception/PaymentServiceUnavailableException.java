package com.example.micro.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

public class PaymentServiceUnavailableException extends RuntimeException {
    public PaymentServiceUnavailableException(String message) {
        super(message);
    }
}
