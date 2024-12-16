package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.AgeCalculatorRequest;
import com.epam.training.gen.ai.dto.WeatherForecastRequest;
import com.epam.training.gen.ai.service.PluginService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v4")
@AllArgsConstructor
public class PluginController {

    private PluginService pluginService;

    @GetMapping("/date-in-french")
    public String dateInFrench() {
        return pluginService.getCurrentDateInFrench();
    }

    @PostMapping("/age-calculator")
    public String calculator(@RequestBody AgeCalculatorRequest request) {
        String birthDay = request.getBirthDay();
        return pluginService.calculateAge(birthDay);
    }

    @PostMapping("/weather-forecast")
    public String weatherForecast(@RequestBody WeatherForecastRequest request) {
        String birthDay = request.getCity();
        return pluginService.forecastWeather(birthDay);
    }

}
