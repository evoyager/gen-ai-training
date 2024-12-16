package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
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

    private final Kernel kernel;
    private final ChatHistory chatHistory;
    private final InvocationContext invocationContext;
    private final PromptExecutionSettings promptExecutionSettings;

    public ChatService(Kernel kernel, InvocationContext invocationContext, PromptExecutionSettings promptExecutionSettings) {
        this.kernel = kernel;
        this.chatHistory = new ChatHistory();
        this.invocationContext = invocationContext;
        this.promptExecutionSettings = promptExecutionSettings;
    }

    public String getChatResponse(String prompt) {
        var response = kernel.invokeAsync(getChat())
                .withArguments(getKernelFunctionArguments(prompt, chatHistory))
                .withPromptExecutionSettings(promptExecutionSettings)
                .withInvocationContext(invocationContext)
                .block();
        chatHistory.addUserMessage(prompt);
        chatHistory.addAssistantMessage(response.getResult());
        log.info("AI answer: " + response.getResult());
        return response.getResult();
    }

    /**
     * Creates a kernel function for generating a chat response using a predefined prompt template.
     * <p>
     * The template includes the chat history and the user's message as variables.
     *
     * @return a {@link KernelFunction} for handling chat-based AI interactions
     */
    private KernelFunction<String> getChat() {
        return KernelFunction.<String>createFromPrompt("""
                        {{$chatHistory}}
                        <message role="user">{{$request}}</message>""")
                .build();
    }

    /**
     * Creates the kernel function arguments with the user prompt and chat history.
     *
     * @param prompt the user's input
     * @param chatHistory the current chat history
     * @return a {@link KernelFunctionArguments} instance containing the variables for the AI model
     */
    private KernelFunctionArguments getKernelFunctionArguments(String prompt, ChatHistory chatHistory) {
        return KernelFunctionArguments.builder()
                .withVariable("request", prompt)
                .withVariable("chatHistory", chatHistory)
                .build();
    }

}