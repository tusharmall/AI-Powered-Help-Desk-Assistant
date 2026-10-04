# Architecture Notes

## 1. Normal REST ticket flow

```text
POST /api/v1/tickets
      |
      v
TicketController
      |
      v
TicketService
      |
      v
TicketRepository
      |
      v
PostgreSQL
```

## 2. AI chat flow

```text
POST /api/v1/ai/chat
      |
      v
AiController
      |
      v
AIService
      |
      v
ChatClient
   |        |        |
   |        |        +--> Tools
   |        |               |
   |        |               +--> TicketService -> PostgreSQL
   |        |               +--> SupportNotificationService -> Mailpit
   |        |
   |        +-----------> RAG Advisor -> PGVector
   |
   +-------------------> Memory Advisor -> JDBC -> PostgreSQL
      |
      v
   OpenAI
      |
      v
 Final answer
```

## 3. RAG indexing flow

```text
knowledge/*.md
      |
      v
Document
      |
      v
TokenTextSplitter
      |
      v
Document chunks
      |
      v
EmbeddingModel
      |
      v
PGVector
```

At question time:

```text
User question
      |
      v
QuestionAnswerAdvisor
      |
      v
Vector similarity search
      |
      v
Top-K relevant chunks
      |
      v
Question + retrieved context
      |
      v
OpenAI Chat Model
      |
      v
Answer
```

## 4. Why this project is useful for a backend developer

It intentionally keeps the familiar enterprise backend layers:

```text
Controller
Service
Repository
Database
```

and adds Spring AI capabilities around the same service layer rather than treating AI as a separate scripting language.