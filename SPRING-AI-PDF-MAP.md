# Mapping This Project to Your Spring AI Notes

Use the 42-page Spring AI PDF as the theory and this repository as the hands-on implementation.

| PDF pages | Concept | Where to study it here |
|---|---|---|
| 29-34 | ChatClient, ChatModel, provider integration | `AIService.java`, `AiConfig.java` |
| 9-12 | System/user roles, prompts, templates | `prompts/helpdesk-system.st`, `AIService.java` |
| 6-8 | Advisors and memory | `AiConfig.java`, `AIService.java` |
| 2-6 | JDBC chat memory, conversation sessions | `application.yml`, `AiConfig.java`, `ConversationId` in `AiController` |
| 14-17 | Entity/structured output | `TicketDraft.java`, `AIService.extractTicket()` |
| 25-28 | Tokens and embeddings | `TokenTextSplitter`, OpenAI embedding configuration |
| 1 | Retrieval-Augmented Generation | `KnowledgeBaseService.java`, `QuestionAnswerAdvisor` |
| 38-42 | Spring AI feature overview | `README.md`, `ARCHITECTURE.md` |

The PDF also distinguishes `ChatClient` from `ChatModel`, explains stateless LLM calls and memory, shows structured entity output, and describes RAG as retrieval + context augmentation + generation. Use those pages while reading the corresponding classes.

## Recommended reading order inside the code

```text
1. HelpdeskApplication.java
2. TicketController.java
3. Ticket.java
4. TicketService.java
5. AiController.java
6. AIService.java
7. AiConfig.java
8. TicketTools.java
9. SupportTools.java
10. KnowledgeBaseService.java
11. application.yml
12. prompts/helpdesk-system.st
```

Then run the requests in the Postman collection and deliberately change one thing at a time.