package com.tusharmall.helpdesk.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tusharmall.helpdesk.entity.Ticket;
import com.tusharmall.helpdesk.service.TicketService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> create(@Valid @RequestBody TicketRequest request) {
        Ticket ticket = Ticket.builder()
                .summary(request.summary())
                .description(request.description())
                .priority(request.priority())
                .category(request.category())
                .email(request.email())
                .build();

        Ticket saved = ticketService.create(ticket);
        return ResponseEntity
                .created(URI.create("/api/v1/tickets/" + saved.getId()))
                .body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getById(@PathVariable @Positive Long id) {
        Ticket ticket = ticketService.getById(id);
        return ticket == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(ticket);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getByEmail(
            @RequestParam @Email String email) {
        return ResponseEntity.ok(ticketService.getUserTickets(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> update(
            @PathVariable @Positive Long id,
            @Valid @RequestBody TicketUpdateRequest request) {

        Ticket existing = ticketService.getById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        existing.setSummary(request.summary());
        existing.setDescription(request.description());
        existing.setPriority(request.priority());
        existing.setCategory(request.category());
        existing.setStatus(request.status());

        return ResponseEntity.ok(ticketService.update(existing));
    }

    public record TicketRequest(
            @NotBlank String summary,
            @NotBlank String description,
            @NotNull com.tusharmall.helpdesk.entity.Priority priority,
            @NotBlank String category,
            @NotBlank @Email String email) {
    }

    public record TicketUpdateRequest(
            @NotBlank String summary,
            @NotBlank String description,
            @NotNull com.tusharmall.helpdesk.entity.Priority priority,
            @NotBlank String category,
            @NotNull com.tusharmall.helpdesk.entity.TicketStatus status) {
    }
}