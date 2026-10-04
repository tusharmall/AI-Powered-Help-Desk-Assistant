package com.tusharmall.helpdesk.controller;

import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.ai.retry.TransientAiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Invalid request");

        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", OffsetDateTime.now().toString(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Validation failed",
                "message", message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", OffsetDateTime.now().toString(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad request",
                "message", ex.getMessage() == null ? "Invalid request" : ex.getMessage()));
    }

    @ExceptionHandler({NonTransientAiException.class, TransientAiException.class})
    ResponseEntity<Map<String, Object>> handleAiException(RuntimeException ex) {
        String message = ex.getMessage() == null ? "AI provider request failed" : ex.getMessage();
        boolean quotaExceeded = message.contains("credit_balance_exhausted")
                || message.contains("insufficient_quota")
                || message.contains("no credits remaining");

        if (quotaExceeded) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of(
                    "timestamp", OffsetDateTime.now().toString(),
                    "status", HttpStatus.TOO_MANY_REQUESTS.value(),
                    "error", "OpenAI quota exhausted",
                    "message", "OpenAI credits are exhausted. Add billing credits or update the API key before retrying."));
        }

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
                "timestamp", OffsetDateTime.now().toString(),
                "status", HttpStatus.BAD_GATEWAY.value(),
                "error", "AI service unavailable",
                "message", "The AI provider is temporarily unavailable. Please try again in a few moments."));
    }
}