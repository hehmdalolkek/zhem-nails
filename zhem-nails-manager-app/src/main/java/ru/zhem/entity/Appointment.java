package ru.zhem.entity;

import java.time.LocalDateTime;

public record Appointment(
        long id,
        User user,
        WorkInterval workInterval,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
