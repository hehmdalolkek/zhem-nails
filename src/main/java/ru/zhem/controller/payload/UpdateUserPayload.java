package ru.zhem.controller.payload;

import jakarta.validation.constraints.Size;
import ru.zhem.entity.constraints.CheckPhoneNumberOrNull;

import java.math.BigDecimal;

public record UpdateUserPayload(

        @CheckPhoneNumberOrNull
        BigDecimal phone,

        @Size(min = 2, max = 32)
        String name,

        @Size(min = 2, max = 32)
        String surname
) {
}
