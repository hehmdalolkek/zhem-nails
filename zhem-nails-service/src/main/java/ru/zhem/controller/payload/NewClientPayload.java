package ru.zhem.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.zhem.entity.constraints.CheckPhoneNumber;

import java.math.BigDecimal;

public record NewClientPayload(
        @CheckPhoneNumber
        BigDecimal phone,

        @NotBlank
        @Size(min = 2, max = 32)
        String name,

        @Size(min = 2, max = 32)
        String surname
) {
}
