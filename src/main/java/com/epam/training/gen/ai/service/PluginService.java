package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.plugin.AgeCalculatorPlugin;
import com.epam.training.gen.ai.plugin.TimePlugin;
import com.epam.training.gen.ai.plugin.WeatherForecastPlugin;
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

    public String calculateAge(String birthDay) {
        Kernel kernel = getKernel();

        KernelFunction<String> prompt = KernelFunctionFromPrompt
                .<String>createFromPrompt(String.format("""
        Convert the age in years {{AgeCalculatorPlugin.ageInYears birthDay="%s"}} to minutes.
    """, birthDay))
                .build();

        FunctionResult<String> ageInDays = prompt.invokeAsync(kernel)
                .block();
        String response = ageInDays.getResult();
        log.info("Response: {}", response);

        return response;

    }

    public String forecastWeather(String city) {
        Kernel kernel = getKernel();

        KernelFunction<String> prompt = KernelFunctionFromPrompt
                .<String>createFromPrompt(String.format("""
        Suggest clothes to wear if weather forecast is: {{WeatherForecastPlugin.forecast city="%s"}}.
    """, city))
                .build();

        FunctionResult<String> ageInDays = prompt.invokeAsync(kernel)
                .block();
        String response = ageInDays.getResult();
        log.info("Response: {}", response);

        return response;

    }

    private Kernel getKernel() {
        ChatCompletionService chat = getChatCompletionService(model);
        KernelPlugin timePlugin = KernelPluginFactory.createFromObject(new TimePlugin(),"TimePlugin");
        KernelPlugin ageCalculatorPlugin = KernelPluginFactory.createFromObject(new AgeCalculatorPlugin(),"AgeCalculatorPlugin");
        KernelPlugin weatherForecastPlugin = KernelPluginFactory.createFromObject(new WeatherForecastPlugin(),"WeatherForecastPlugin");


        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chat)
                .withPlugin(timePlugin)
                .withPlugin(ageCalculatorPlugin)
                .withPlugin(weatherForecastPlugin)
                .build();
    }

    private ChatCompletionService getChatCompletionService(String model) {
        return OpenAIChatCompletion.builder()
                .withModelId(model)
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }
}
