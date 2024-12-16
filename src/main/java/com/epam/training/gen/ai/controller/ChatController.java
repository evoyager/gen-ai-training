package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatRequest;
import com.epam.training.gen.ai.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
@AllArgsConstructor
public class ChatController {

    private ChatService chatService;

    @PostMapping("/chat")
    public String chatWithBot(@RequestBody ChatRequest request) {
        String prompt = request.getInput();

        return chatService.getChatResponse(prompt);
    }
}
