package com.substring.auth.auth_security_app_backend.dtos;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String message,
        HttpStatus status,
        int statusCode
        ) {
}
