package ru.zhem.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record Client(
        BigDecimal phone,
        String name,
        String surname,

        String password,
        LocalDateTime createdAt
) {
}
