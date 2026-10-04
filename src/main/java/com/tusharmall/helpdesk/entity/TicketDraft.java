package com.tusharmall.helpdesk.entity;

/**
 * Small DTO used to demonstrate Spring AI structured output.
 * The model is asked to convert free-form text into this Java type.
 */
public record TicketDraft(
        String summary,
        String description,
        Priority priority,
        String category,
        String email) {
}