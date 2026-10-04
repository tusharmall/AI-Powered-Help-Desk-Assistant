package com.tusharmall.helpdesk.repository;

import com.tusharmall.helpdesk.entity.Ticket;
import com.tusharmall.helpdesk.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByEmailOrderByUpdatedOnDesc(String email);

    List<Ticket> findByEmailAndStatusInOrderByUpdatedOnDesc(
            String email,
            List<TicketStatus> statuses);
}