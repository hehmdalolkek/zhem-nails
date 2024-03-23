package ru.zhem.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.zhem.entity.User;
import ru.zhem.entity.WorkInterval;

public record UpdateAppointmentPayload(

        @Min(1)
        Long userId,

        @Min(1)
        Long workIntervalId,

        @Size(min = 2, max = 500)
        String details
) {
}
