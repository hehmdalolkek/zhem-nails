package ru.zhem.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewAppointmentPayload(
        @NotNull
        @Min(1)
        Long userId,

        @NotNull
        @Min(1)
        Long workIntervalId,

        @Size(min = 2, max = 500)
        String details
) {
}
