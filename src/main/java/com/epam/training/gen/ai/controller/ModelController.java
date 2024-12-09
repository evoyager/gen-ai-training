package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatRequest;
import com.epam.training.gen.ai.service.ModelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3")
@AllArgsConstructor
public class ModelController {

    private ModelService modelService;

    @PostMapping("/chat")
    public String chatWithBot(@RequestBody ChatRequest request) {
        return modelService.getChatResponse(request);
    }

}
