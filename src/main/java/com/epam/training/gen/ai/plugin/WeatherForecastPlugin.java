package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class WeatherForecastPlugin {
    public static final DateTimeFormatter DAY_MONTH_YEAR = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final String WEATHER_FORECAST = "Temperature: 19°C to 24°C\n" +
            "Humidity: Moderate at 53%–66%";
    public static final LocalDate tomorrowDate = LocalDate.now().plusDays(1);

    /**
     * Get one day weather forecast for the USA city.
     *
     * <p>Example: {{weather.forecast}} => Temperature: 16°C to 25°C
     *
     * @return one day weather forecast for the USA city.
     */
    @DefineKernelFunction(
            name = "forecast",
            description = "Get weather forecast")
    public String forecast(
            @KernelFunctionParameter(
                    name = "city",
                    description = "USA city to get one day weather forecast"
            )
            String city) {
        // Example: Miami, Florida

        log.info("Weather plugin was called to forecast weather in: [{}]", city);
        return String.format("Tomorrow %s in %s city weather will be: %s",
                tomorrowDate.format(DAY_MONTH_YEAR), city, WEATHER_FORECAST);
    }
}
