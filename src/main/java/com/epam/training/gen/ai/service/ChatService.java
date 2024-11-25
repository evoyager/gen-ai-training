package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for generating chat completions using Azure OpenAI.
 * <p>
 * This service interacts with the Azure OpenAI API to generate chat completions
 * based on input message. It retrieves responses from the AI model
 * and logs them.
 */
@Slf4j
@Service
public class ChatService {

    private final OpenAIAsyncClient aiAsyncClient;
    private final String deploymentOrModelName;

    public ChatService(OpenAIAsyncClient aiAsyncClient,
                               @Value("${client-openai-deployment-name}") String deploymentOrModelName) {
        this.aiAsyncClient = aiAsyncClient;
        this.deploymentOrModelName = deploymentOrModelName;
    }

    public Object getChatCompletions(String prompt) {
        var completions = aiAsyncClient
                .getChatCompletions(
                        deploymentOrModelName,
                        new ChatCompletionsOptions(
                                List.of(new ChatRequestUserMessage(prompt))))
                .block();
        var messages = completions.getChoices().stream()
                .map(c -> c.getMessage().getContent())
                .toList();
        log.info(messages.toString());
        return messages;
    }
}