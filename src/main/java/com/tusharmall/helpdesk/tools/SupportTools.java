package com.tusharmall.helpdesk.tools;

import com.tusharmall.helpdesk.entity.Ticket;
import com.tusharmall.helpdesk.service.SupportNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupportTools {

    private final SupportNotificationService notificationService;

    @Tool(description = "Send a notification email to the support team after a new ticket is created.")
    public String notifySupport(
            @ToolParam(description = "The newly created ticket") Ticket ticket) {
        notificationService.notifySupport(ticket);
        return "Support notification email sent for ticket #" + ticket.getId();
    }
}