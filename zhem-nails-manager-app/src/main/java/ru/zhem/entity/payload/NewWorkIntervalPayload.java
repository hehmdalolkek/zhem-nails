package ru.zhem.entity.payload;

import java.time.LocalDate;
import java.time.LocalTime;

public record NewWorkIntervalPayload(
        LocalDate date,
        LocalTime startTime
) {
}
