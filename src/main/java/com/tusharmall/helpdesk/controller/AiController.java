package com.tusharmall.helpdesk.controller;

import com.tusharmall.helpdesk.entity.TicketDraft;
import com.tusharmall.helpdesk.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AiController {

    private final AIService aiService;

    @PostMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestBody String query,
            @RequestHeader("ConversationId") String conversationId) {
        return ResponseEntity.ok(aiService.answer(query, conversationId));
    }

    @PostMapping(value = "/stream", produces = "text/event-stream")
    public Flux<String> stream(
            @RequestBody String query,
            @RequestHeader("ConversationId") String conversationId) {
        return aiService.stream(query, conversationId);
    }

    @PostMapping("/extract-ticket")
    public ResponseEntity<TicketDraft> extractTicket(@RequestBody String rawText) {
        return ResponseEntity.ok(aiService.extractTicket(rawText));
    }
}