package com.tusharmall.helpdesk.controller;

import com.tusharmall.helpdesk.service.KnowledgeBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

    private final KnowledgeBaseService knowledgeBaseService;

    @PostMapping("/reindex")
    public ResponseEntity<Map<String, Object>> reindex() {
        int count = knowledgeBaseService.reindexSampleKnowledgeBase();
        return ResponseEntity.ok(Map.of(
                "message", "Knowledge base indexed successfully",
                "chunkCount", count));
    }
}