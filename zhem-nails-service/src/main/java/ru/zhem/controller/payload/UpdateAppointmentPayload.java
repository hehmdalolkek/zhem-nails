package ru.zhem.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateAppointmentPayload(

        BigDecimal phone,

        @Min(1)
        Long workIntervalId,

        @Size(min = 2, max = 500)
        String details
) {
}
