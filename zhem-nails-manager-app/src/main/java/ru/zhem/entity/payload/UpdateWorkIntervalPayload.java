package ru.zhem.entity.payload;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateWorkIntervalPayload(
        @NotNull
        LocalDate date,
        @NotNull
        LocalTime startTime
) {
}
