package ru.zhem.controller.payload;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record NewWorkIntervalPayload(
        @NotNull
        LocalDate date,

        @NotNull
        LocalTime startTime
) {
}
