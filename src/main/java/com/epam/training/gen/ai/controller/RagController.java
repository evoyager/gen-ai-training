package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.RagChatRequest;
import com.epam.training.gen.ai.service.RagService;
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

    //TODO: create endpoint to load knowledge from request sources

    @PostMapping("/rag/chat")
    public ResponseEntity<String> chatViaRag(@RequestBody RagChatRequest request) {
        String response = ragService.chatViaRag(request.getInput());
        return ResponseEntity.ok(String.format("Response from RAG: {%s}", response));
    }
}
