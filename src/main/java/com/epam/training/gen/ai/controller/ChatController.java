package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatRequest;
import com.epam.training.gen.ai.dto.ChatResponse;
import com.epam.training.gen.ai.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
@AllArgsConstructor
public class ChatController {

    private ChatService chatService;

    @PostMapping("/chat")
    public ChatResponse chatWithBot(@RequestBody ChatRequest request) {
        String prompt = request.getInput();

        var messages = chatService.getChatResponse(prompt);
        return new ChatResponse(List.of(messages));
    }
}
