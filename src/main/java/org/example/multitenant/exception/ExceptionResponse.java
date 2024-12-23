package org.example.multitenant.exception;

public record ExceptionResponse(
        String code,
        String message
) {
}
