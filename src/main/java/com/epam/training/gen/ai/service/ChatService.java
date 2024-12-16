package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.implementation.CollectionUtil;
import com.microsoft.semantickernel.orchestration.InvocationContext;
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
public class ChatService {

    private final ChatCompletionService chat;
    private final Kernel kernel;
    private final ChatHistory history;
    private final InvocationContext invocationContext;

    public ChatService(ChatCompletionService chat, Kernel kernel, InvocationContext invocationContext) {
        this.chat = chat;
        this.kernel = kernel;
        this.history = new ChatHistory();
        this.invocationContext = invocationContext;
    }

    public String getChatResponse(String input) {
        history.addUserMessage(input);

        var result = chat.getChatMessageContentsAsync(history, kernel, invocationContext)
                .block();

        String response = CollectionUtil.getLastOrNull(result).getContent();
        history.addAssistantMessage(response);
        log.info("Response: {}", response);

        return response;
    }

}