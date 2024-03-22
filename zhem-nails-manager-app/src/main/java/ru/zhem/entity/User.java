package ru.zhem.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record User(
        long id,
        BigDecimal phone,
        String name,
        String surname,
        LocalDateTime createdAt
) {
}
