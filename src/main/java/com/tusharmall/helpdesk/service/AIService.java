package com.tusharmall.helpdesk.service;

import com.tusharmall.helpdesk.entity.TicketDraft;
import com.tusharmall.helpdesk.tools.SupportTools;
import com.tusharmall.helpdesk.tools.TicketTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    private final ChatClient chatClient;
    private final TicketTools ticketTools;
    private final SupportTools supportTools;

    public String answer(String query, String conversationId) {
        return chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .tools(ticketTools, supportTools)
                .user(query)
                .call()
                .content();
    }

    public Flux<String> stream(String query, String conversationId) {
        return chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .tools(ticketTools, supportTools)
                .user(query)
                .stream()
                .content();
    }

    public TicketDraft extractTicket(String rawText) {
        String prompt = "Extract a help desk ticket from this text. "
                + "Return only the structured fields. Infer priority only when the business impact is clear. "
                + "If the email is missing, leave it null.\n\n"
                + rawText;

        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(TicketDraft.class);
    }

    public String simpleChatWithoutRag(String query) {
        return chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "stateless-demo"))
                .user(query)
                .call()
                .content();
    }
}