package ru.zhem.entity;

import java.time.LocalDateTime;

public record Appointment(
        long id,
        User user,
        String details,
        WorkInterval workInterval,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
