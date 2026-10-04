package com.tusharmall.helpdesk.tools;

import com.tusharmall.helpdesk.entity.Ticket;
import com.tusharmall.helpdesk.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketTools {

    private final TicketService ticketService;

    @Tool(description = "Find open or in-progress tickets for a user's email before creating a duplicate ticket.")
    public List<Ticket> findOpenTickets(
            @ToolParam(description = "The user's email address") String email) {
        return ticketService.getOpenTickets(email);
    }

    @Tool(description = "Create a new support ticket in the ticket database.")
    public Ticket createTicket(
            @ToolParam(description = "Complete ticket object with summary, description, priority, category, status and email") Ticket ticket) {
        return ticketService.create(ticket);
    }

    @Tool(description = "Update an existing support ticket.")
    public Ticket updateTicket(
            @ToolParam(description = "Ticket object containing the existing ID and the fields to update") Ticket ticket) {
        return ticketService.update(ticket);
    }

    @Tool(description = "Get one support ticket by ID.")
    public Ticket getTicket(
            @ToolParam(description = "Ticket ID") Long ticketId) {
        return ticketService.getById(ticketId);
    }

    @Tool(description = "Return the current server time in ISO-8601 format.")
    public String getCurrentTime() {
        return OffsetDateTime.now().toString();
    }
}