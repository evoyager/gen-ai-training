package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatRequest;
import com.epam.training.gen.ai.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<Object> chatWithBot(@RequestBody ChatRequest chatRequest) {
        String prompt = chatRequest.getInput();
        Object response = chatService.getChatCompletions(prompt);
        return ResponseEntity.ok(response);
    }
}
