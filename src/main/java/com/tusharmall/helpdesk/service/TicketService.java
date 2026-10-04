package com.tusharmall.helpdesk.service;

import com.tusharmall.helpdesk.entity.Ticket;
import com.tusharmall.helpdesk.entity.TicketStatus;
import com.tusharmall.helpdesk.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional
    public Ticket create(Ticket ticket) {
        ticket.setId(null);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket update(Ticket ticket) {
        if (ticket.getId() == null || !ticketRepository.existsById(ticket.getId())) {
            throw new IllegalArgumentException("Ticket does not exist: " + ticket.getId());
        }
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public Ticket getById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getUserTickets(String email) {
        return ticketRepository.findByEmailOrderByUpdatedOnDesc(email);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getOpenTickets(String email) {
        return ticketRepository.findByEmailAndStatusInOrderByUpdatedOnDesc(
                email,
                List.of(TicketStatus.OPEN, TicketStatus.IN_PROGRESS));
    }
}