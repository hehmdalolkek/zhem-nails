package ru.zhem.entity;

import java.time.LocalDate;
import java.time.LocalTime;


public record WorkInterval(
        long id,
        LocalDate date,
        LocalTime startTime,
        Boolean isBooked
) {
}
