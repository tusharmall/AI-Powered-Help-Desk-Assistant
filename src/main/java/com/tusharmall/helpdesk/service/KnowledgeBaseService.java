package com.tusharmall.helpdesk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KnowledgeBaseService {

    private final VectorStore vectorStore;
    private final ApplicationContext applicationContext;

    private final TokenTextSplitter splitter =
            new TokenTextSplitter(350, 120, 50, 100, true, List.of('.', '!', '?', '\n'));

    @Value("${app.rag.source-filter:knowledgeBase == 'helpdesk'}")
    private String sourceFilter;

    /**
     * Load sample Markdown files into PGVector. Existing sample chunks are removed first,
     * which keeps the sample knowledge base deterministic for learning.
     */
    public synchronized int reindexSampleKnowledgeBase() {
        vectorStore.delete(sourceFilter);

        List<Document> chunks = new ArrayList<>();
        try {
            Resource[] resources = applicationContext.getResources("classpath*:knowledge/*.md");
            for (Resource resource : resources) {
                String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                String source = resource.getFilename() == null ? "unknown.md" : resource.getFilename();
                String checksum = sha256(content);

                Document sourceDocument = new Document(
                        content,
                        Map.of(
                                "knowledgeBase", "helpdesk",
                                "source", source,
                                "checksum", checksum));

                chunks.addAll(splitter.split(sourceDocument));
            }

            vectorStore.add(chunks);
            return chunks.size();
        }
        catch (IOException e) {
            throw new IllegalStateException("Failed to read knowledge-base resources", e);
        }
    }

    private String sha256(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to calculate checksum", e);
        }
    }
}