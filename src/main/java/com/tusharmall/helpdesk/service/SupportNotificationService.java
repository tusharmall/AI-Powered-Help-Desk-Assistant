package com.tusharmall.helpdesk.service;

import com.tusharmall.helpdesk.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupportNotificationService {

    private final JavaMailSender mailSender;

    @Value("${app.support-email}")
    private String supportEmail;

    public void notifySupport(Ticket ticket) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(supportEmail);
        message.setSubject("New Help Desk Ticket #" + ticket.getId());
        message.setText("A new help desk ticket was created.\n\n"
                + "Ticket ID: " + ticket.getId() + "\n"
                + "Summary: " + ticket.getSummary() + "\n"
                + "Priority: " + ticket.getPriority() + "\n"
                + "Category: " + ticket.getCategory() + "\n"
                + "User: " + ticket.getEmail() + "\n\n"
                + ticket.getDescription());
        mailSender.send(message);
    }
}