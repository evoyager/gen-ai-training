package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    private final ChatHistory chatHistory;

    public ChatService(OpenAIAsyncClient aiAsyncClient,
                               @Value("${client-openai-deployment-name}") String deploymentOrModelName) {
        this.chatHistory = new ChatHistory();
        this.aiAsyncClient = aiAsyncClient;
        this.deploymentOrModelName = deploymentOrModelName;
    }

    public Mono<String> getChatResponse(String input, PromptExecutionSettings settings) {
        chatHistory.addUserMessage(input);

        ChatRequestUserMessage userMessage = new ChatRequestUserMessage(input);
        ChatCompletionsOptions options = new ChatCompletionsOptions(List.of(userMessage));
        options.setTemperature(settings.getTemperature());

        return aiAsyncClient
                .getChatCompletions(deploymentOrModelName, options)
                .map(ChatCompletions::getChoices)
                .flatMap(choices -> {
                    if (choices != null && !choices.isEmpty()) {
                        String response = choices.get(0).getMessage().getContent();
                        chatHistory.addSystemMessage(response);
                        log.info("Chat history: {}", chatHistory.getMessages().toString());
                        return Mono.just(response);
                    }
                    return Mono.just("No response received");
                });
    }

}