package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatRequest;
import com.epam.training.gen.ai.service.ChatService;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public Mono<String> chatWithBot(@RequestBody ChatRequest request) {
        String prompt = request.getInput();
        PromptExecutionSettings settings = PromptExecutionSettings.builder()
                .withTemperature(request.getTemperature())
                .build();

        return chatService.getChatResponse(prompt, settings);
    }
}
