package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.RagChatRequest;
import com.epam.training.gen.ai.dto.RagDocumentsRequest;
import com.epam.training.gen.ai.service.RagService;
import dev.langchain4j.store.embedding.IngestionResult;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v6")
@AllArgsConstructor
public class RagController {

    private final RagService ragService;

    @PostMapping("/rag/documents")
    public ResponseEntity<String> loadDocuments(@RequestBody RagDocumentsRequest request) {
        String path = request.getPath();
        IngestionResult response = ragService.loadDocuments(path);
        return ResponseEntity.ok(String.format("Documents successfully loaded to RAG from path: [\"%s\"].\n %s", path, response.tokenUsage()));
    }

    @PostMapping("/rag/chat")
    public ResponseEntity<String> chatViaRag(@RequestBody RagChatRequest request) {
        String response = ragService.chatViaRag(request.getInput());
        return ResponseEntity.ok(String.format("Response from AI using RAG: {%s}", response));
    }
}
