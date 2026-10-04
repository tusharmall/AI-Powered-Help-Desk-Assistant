# How to Learn This Project

Use this repository as a single Spring AI lab. The objective is not to memorize code; it is to understand the request flow and then modify the code yourself.

## Day 1 — Run the base application

1. Start Docker Compose.
2. Set `OPENAI_API_KEY`.
3. Start Spring Boot.
4. Open `http://localhost:8081/`.
5. Run the normal `/api/v1/tickets` REST APIs in Postman.

Learn:

```text
Controller -> Service -> Repository -> PostgreSQL
```

## Day 2 — ChatClient

Read `AIService.answer()` from top to bottom.

Understand each call separately:

```java
chatClient.prompt()
.advisors(...)
.tools(...)
.user(query)
.call()
.content();
```

Then temporarily remove `.tools(...)` and see how the behavior changes.

## Day 3 — Prompt engineering

Read `src/main/resources/prompts/helpdesk-system.st` line by line.

Change one instruction at a time and observe the answer.

Then add a rule such as:

```text
Never ask users for passwords, MFA codes, API keys, or private keys.
```

## Day 4 — Memory

Use the same `ConversationId` for two requests.

Then use a different `ConversationId`.

Observe the difference.

Trace:

```text
ConversationId
    -> MessageChatMemoryAdvisor
    -> ChatMemory
    -> JDBC repository
    -> PostgreSQL
```

## Day 5 — RAG

Run `/api/v1/knowledge/reindex`.

Ask a question that is clearly answered by `vpn-troubleshooting.md`.

Then ask something that is not present.

Read `KnowledgeBaseService` and identify:

```text
resource
 -> Document
 -> TokenTextSplitter
 -> EmbeddingModel
 -> PGVector
 -> QuestionAnswerAdvisor
 -> prompt context
 -> LLM
```

## Day 6 — Tool calling

Ask the assistant to create a ticket.

Use PostgreSQL to verify the row was created.

Read `TicketTools` line by line.

The key idea is:

```text
LLM decides
    -> tool call request
    -> Spring AI executes Java method
    -> Java method calls application service
    -> database changes
    -> result goes back to the model
    -> final response
```

## Day 7 — Structured output and streaming

Run `/api/v1/ai/extract-ticket` and inspect the Java `TicketDraft` record.

Then compare:

```text
call().content()
```

with:

```text
stream().content()
```

## After you understand the project

Implement these upgrades yourself, in this order:

1. Add authentication with Spring Security + JWT.
2. Add ticket pagination/filtering/sorting.
3. Replace sample Markdown-only ingestion with PDF ingestion.
4. Add metadata filters such as product/team/category.
5. Add an evaluator test for RAG answer relevance.
6. Add observability and request IDs.
7. Add Docker image build and a deployment target.
8. Add an MCP server for ticket tools as a separate module.

Do not add a feature to your resume until you can explain its code and demonstrate it locally.