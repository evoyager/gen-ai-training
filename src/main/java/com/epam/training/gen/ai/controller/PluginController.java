package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.PluginService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v4")
@AllArgsConstructor
public class PluginController {

    private PluginService pluginService;

    @GetMapping("/chat")
    public String chatWithBot() {
        return pluginService.getChatResponse();
    }
}
