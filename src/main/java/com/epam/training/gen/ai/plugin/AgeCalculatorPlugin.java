package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgeCalculatorPlugin {
    public static final String DAY_MONTH_DAY_YEAR = "EEEE, MMMM d, yyyy";

    /**
     * Get the age in days.
     *
     * <p>Example: {{age.ageInDays}} => 8395
     *
     * @return The age in days.
     */
    @DefineKernelFunction(
            name = "ageInDays",
            description = "Calculate expression")
    public Long ageInDays(
            @KernelFunctionParameter(
                    name = "birthDay",
                    description = "Birthday to calculate age in days"
            )
            String birthDay) {
        // Example: Sunday, January 12, 2001

        log.info("Simple plugin was called with birthday: [{}]", birthDay);
        return (long) (23 * 365);
    }
}
