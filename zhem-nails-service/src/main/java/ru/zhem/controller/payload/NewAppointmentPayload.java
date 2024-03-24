package ru.zhem.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.zhem.entity.constraints.CheckPhoneNumber;

import java.math.BigDecimal;

public record NewAppointmentPayload(

        BigDecimal phone,

        @NotNull
        @Min(1)
        Long workIntervalId,

        @Size(min = 2, max = 500)
        String details
) {
}
