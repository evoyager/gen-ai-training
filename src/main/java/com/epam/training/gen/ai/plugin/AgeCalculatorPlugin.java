package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Slf4j
public class AgeCalculatorPlugin {
    public static final DateTimeFormatter DAY_MONTH_YEAR = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Get the age in days.
     *
     * <p>Example: {{age.ageInYears}} => 8395
     *
     * @return The age in days.
     */
    @DefineKernelFunction(
            name = "ageInYears",
            description = "Calculate age in years")
    public String ageInYears(
            @KernelFunctionParameter(
                    name = "birthDay",
                    description = "Birthday to calculate age in years"
            )
            String birthDay) {
        // Example: 12-01-2001
        LocalDate birthDate = LocalDate.parse(birthDay, DAY_MONTH_YEAR);
        LocalDate currentDate = LocalDate.now();

        log.info("Simple plugin was called with birthday: [{}]", birthDay);
        return String.valueOf(Period.between(birthDate, currentDate).getYears());
    }
}
