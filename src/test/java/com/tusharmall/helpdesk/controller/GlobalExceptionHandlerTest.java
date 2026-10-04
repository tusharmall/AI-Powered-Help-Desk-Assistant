package com.tusharmall.helpdesk.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handlesOpenAiQuotaErrors() {
        ResponseEntity<Map<String, Object>> response = handler.handleAiException(
                new NonTransientAiException("HTTP 429 - {\"error\":{\"code\":\"credit_balance_exhausted\"}}"));

        assertNotNull(response);
        Map<String, Object> body = response.getBody();

        assertNotNull(body);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertEquals("OpenAI quota exhausted", body.get("error"));
        assertEquals("OpenAI credits are exhausted. Add billing credits or update the API key before retrying.",
                body.get("message"));
    }
}
