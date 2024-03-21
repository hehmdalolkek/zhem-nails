package ru.zhem.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record NewAppointmentPayload(
        @NotNull
        @Min(1)
        Long userId,

        @NotNull
        @Min(1)
        Long workIntervalId
) {
}
