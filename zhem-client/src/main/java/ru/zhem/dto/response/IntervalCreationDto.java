package ru.zhem.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntervalCreationDto {

    @NotNull(message = "Поле должно быть заполнено")
    private LocalDate date;

    @NotNull(message = "Поле должно быть заполнено")
    private LocalTime time;

}
