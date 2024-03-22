package ru.zhem.controller.payload;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateWorkIntervalPayload(
        LocalDate date,

        LocalTime startTime,

        Boolean isBooked
) {
}
