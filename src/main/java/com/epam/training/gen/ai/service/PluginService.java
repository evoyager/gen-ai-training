package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.plugin.TimePlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionFromPrompt;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class for generating chat completions using Azure OpenAI.
 * <p>
 * This service interacts with the Azure OpenAI API to generate chat completions
 * based on input message with additional data from Kernel plugins. It retrieves responses from the AI model
 * and logs them.
 */
@Slf4j
@Service
public class PluginService {
    @Value("${client-openai-deployment-name}")
    String model;

    private final OpenAIAsyncClient openAIAsyncClient;


    public PluginService(OpenAIAsyncClient openAIAsyncClient) {
        this.openAIAsyncClient = openAIAsyncClient;
    }

    public String getCurrentDateInFrench() {
        Kernel kernel = getKernel();

        KernelFunction<String> prompt = KernelFunctionFromPrompt
                .<String>createFromPrompt("""
        Translate the date {{TimePlugin.date locale="English"}} to French.
    """)
                .build();

        FunctionResult<String> timeInFrench = prompt.invokeAsync(kernel)
                .block();
        String response = timeInFrench.getResult();
        log.info("Response: {}", response);

        return response;
    }

    private Kernel getKernel() {
        ChatCompletionService chat = getChatCompletionService(model);
        KernelPlugin timePlugin = KernelPluginFactory.createFromObject(new TimePlugin(),"TimePlugin");

        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chat)
                .withPlugin(timePlugin)
                .build();
    }

    private ChatCompletionService getChatCompletionService(String model) {
        return OpenAIChatCompletion.builder()
                .withModelId(model)
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }
}