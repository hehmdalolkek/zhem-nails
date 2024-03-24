package ru.zhem.entity.payload;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateWorkIntervalPayload(
        LocalDate date,
        LocalTime startTime
) {
}
