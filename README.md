# AI-Powered Help Desk Assistant

A resume-oriented Java/Spring Boot project that demonstrates the main Spring AI concepts from your learning notes in one coherent application:

- `ChatClient` and fluent prompts
- OpenAI chat model integration
- Prompt management through an external `.st` system prompt
- `MessageChatMemoryAdvisor` with JDBC persistence
- Conversation IDs for multi-session chat
- RAG with OpenAI embeddings + PostgreSQL/PGVector + `QuestionAnswerAdvisor`
- Token-based document chunking with `TokenTextSplitter`
- AI tool calling with `@Tool` / `@ToolParam`
- Database actions through AI tools
- Real local email delivery through Mailpit
- Structured output from free text into a Java `record`
- Reactive streaming with `Flux<String>`
- Conventional ticket CRUD APIs with validation and centralized exception handling
- Docker Compose for PostgreSQL/PGVector and Mailpit
- Small browser UI served by Spring Boot

Spring AI 1.1.8 is the latest 1.x release line; the Spring AI release notes show the 1.1.x line as the Boot 3.x-compatible branch, while Spring AI 2.0 is a Boot 4.x line. This project intentionally stays on the 1.1.x line so the code remains close to the Spring Boot 3.x ecosystem used in the rest of your resume.




<img width="1450" height="655" alt="Screenshot 2026-10-04 162346" src="https://github.com/user-attachments/assets/07013f0c-08fc-4736-a1b0-85263400ba0e" />
<img width="1357" height="591" alt="Screenshot 2026-10-04 162357" src="https://github.com/user-attachments/assets/821dd22f-40a7-4a18-b724-9aad9be511be" />


## Architecture

```text
Browser / Postman
       |
       v
Spring Boot REST Controller
       |
       v
     AIService
       |
       v
    ChatClient
       |
       +-----------------------------+
       |                             |
       v                             v
Memory Advisor                  RAG Advisor
       |                             |
       v                             v
JDBC Chat Memory              PGVector similarity search
       |                             |
       +-------------+---------------+
                     |
                     v
                   OpenAI
                     |
                     +------------------+
                     |                  |
                     v                  v
               Tool Calling        Final Answer
                 |     |
                 |     +----> Mailpit / Email
                 |
                 +----------> Ticket Service -> PostgreSQL
```

## Prerequisites

Install:

1. JDK 21
2. Docker Desktop
3. Git
4. Maven 3.9+ OR use your Maven Wrapper if you add one in your IDE
5. An OpenAI API key

## Run it

### 1. Start the local infrastructure

```bash
docker compose up -d
```

This starts:

- PostgreSQL + PGVector on `localhost:5432`
- Mailpit SMTP on `localhost:1025`
- Mailpit web UI on `http://localhost:8025`

### 2. Configure the API key

Windows PowerShell:

```powershell
$env:OPENAI_API_KEY="your-real-key"
$env:OPENAI_CHAT_MODEL="gpt-4o-mini"
$env:OPENAI_EMBEDDING_MODEL="text-embedding-3-small"
```

Linux/macOS:

```bash
export OPENAI_API_KEY="your-real-key"
export OPENAI_CHAT_MODEL="gpt-4o-mini"
export OPENAI_EMBEDDING_MODEL="text-embedding-3-small"
```

### 3. Start the application

```bash
mvn spring-boot:run
```

### 4. Index the knowledge base

```bash
curl -X POST http://localhost:8081/api/v1/knowledge/reindex
```

Or open:

`http://localhost:8081/`

and press **Reindex Knowledge**.

### 5. Ask a RAG question

```bash
curl -X POST http://localhost:8081/api/v1/ai/chat \
  -H "Content-Type: text/plain" \
  -H "ConversationId: demo-1" \
  --data "My VPN is not connecting and I get authentication failures. What should I check?"
```

### 6. See the email

After a new ticket is created, open:

`http://localhost:8025`

Mailpit will show the support notification locally. No real external email is sent.

## Endpoints

| Method | Endpoint | What it demonstrates |
|---|---|---|
| POST | `/api/v1/knowledge/reindex` | Document ingestion + chunking + embeddings + PGVector |
| POST | `/api/v1/ai/chat` | ChatClient + memory + RAG + tool calling |
| POST | `/api/v1/ai/stream` | Reactive streaming |
| POST | `/api/v1/ai/extract-ticket` | Structured output into Java `record` |
| POST | `/api/v1/tickets` | Normal REST ticket creation |
| GET | `/api/v1/tickets/{id}` | Ticket lookup |
| GET | `/api/v1/tickets?email=...` | User ticket history |
| PUT | `/api/v1/tickets/{id}` | Ticket update |

Import `postman/AI-Helpdesk.postman_collection.json` into Postman for ready-made requests.

## Learning order

Do not try to memorize the entire repository at once.

### Phase 1 — ChatClient

Start with `AIService.answer()` and understand:

```java
chatClient.prompt()
    .user(query)
    .call()
    .content();
```

### Phase 2 — Prompt

Open `prompts/helpdesk-system.st` and understand the difference between:

- system instruction
- user message
- retrieved context

### Phase 3 — Memory

Read `AiConfig.chatMemory()` and `MessageChatMemoryAdvisor`.

Then change the `ConversationId` in Postman and observe that a different conversation is created.

### Phase 4 — RAG

Read `KnowledgeBaseService`.

Trace:

```text
Markdown
  -> Document
  -> TokenTextSplitter
  -> EmbeddingModel
  -> PGVector
  -> QuestionAnswerAdvisor
  -> prompt context
  -> LLM answer
```

### Phase 5 — Tool calling

Open `TicketTools` and `SupportTools`.

Learn:

```java
@Tool
@ToolParam
chatClient ... .tools(...)
```

### Phase 6 — Structured output

Read `TicketDraft` and `AIService.extractTicket()`.

Trace:

```text
plain English
    -> LLM
    -> JSON/schema-like output
    -> TicketDraft Java record
```

### Phase 7 — Streaming

Compare:

```text
call().content()
```

with:

```text
stream().content()
```

## What to say in an interview

> I built an AI-powered help desk assistant using Java, Spring Boot and Spring AI. The application uses ChatClient for model interaction, JDBC-backed conversation memory for multi-turn sessions, PGVector with embeddings for RAG over internal troubleshooting documents, and Spring AI tool calling to let the model look up and modify tickets. I also added structured-output mapping and reactive streaming, and containerized PostgreSQL/PGVector and Mailpit for local development.

Be able to explain every sentence before adding it to your resume.

## Suggested resume project entry

**AI-Powered Help Desk Assistant | Java, Spring Boot, Spring AI, OpenAI, RAG, PGVector, PostgreSQL, Docker**

- Developed an AI-powered help-desk backend using Spring Boot and Spring AI, exposing REST APIs for conversational support, structured ticket extraction, and streaming responses.
- Implemented Retrieval-Augmented Generation using OpenAI embeddings, PostgreSQL/PGVector, token-based document chunking, and similarity search to ground responses in internal troubleshooting knowledge.
- Implemented JDBC-backed conversational memory with conversation IDs and Spring AI advisors to preserve context across multi-turn user sessions.
- Implemented AI tool calling for ticket lookup, creation, update, and support notifications, integrating model decisions with Spring Data JPA and PostgreSQL.

These bullets describe the intended completed project. Keep a bullet only after you have personally run and understood the feature it describes.

## Important notes

- Never commit `.env` or a real API key.
- The email integration points to Mailpit, so development is safe and local.
- The sample knowledge base is small on purpose; the goal is to learn the pipeline and then extend it with PDFs, metadata filters, authentication, evaluation, and production deployment.
- The project intentionally uses one PostgreSQL instance for application data, JDBC chat memory, and PGVector so the local setup stays simple.
