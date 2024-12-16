package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.dto.ChatRequest;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.implementation.CollectionUtil;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service class for generating chat completions using Azure OpenAI.
 * <p>
 * This service interacts with the Azure OpenAI API to generate chat completions
 * based on input message. It retrieves responses from the AI model
 * and logs them. Also, it initiates ChatHistory for storing all user and system messages to ChatHistory.
 * It uses ChatHistory to provide context about previous messages for an AI model.
 */
@Slf4j
@Service
public class ModelService {

    OpenAIAsyncClient openAIAsyncClient;
    private ChatCompletionService chat;
    private final ChatHistory history;

    public ModelService(OpenAIAsyncClient openAIAsyncClient) {
        this.openAIAsyncClient = openAIAsyncClient;
        this.history = new ChatHistory();
    }

    public String getChatResponse(ChatRequest request) {
        chat = getChatCompletionService(request.getModel());
        Kernel kernel = getKernel();
        InvocationContext invocationContext = getInvocationContext(request.getTemperature());
        history.addUserMessage(request.getInput());

        var result = chat.getChatMessageContentsAsync(history, kernel, invocationContext)
                .block();

        String response = CollectionUtil.getLastOrNull(result).getContent();
        history.addAssistantMessage(response);
        log.info("Response: {}", response);

        return response;
    }

    private ChatCompletionService getChatCompletionService(String model) {
        return OpenAIChatCompletion.builder()
                .withModelId(model)
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

    private Kernel getKernel() {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chat)
                .build();
    }

    private InvocationContext getInvocationContext(double temperature) {
        return InvocationContext.builder()
                .withPromptExecutionSettings(PromptExecutionSettings.builder()
                        .withTemperature(temperature)
                        .build())
                .build();
    }
}
