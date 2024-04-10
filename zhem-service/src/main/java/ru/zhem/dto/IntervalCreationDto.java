package ru.zhem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntervalCreationDto {

    @NotNull(message = "Поле должно быть заполнено")
    private LocalDate date;

    @NotNull(message = "Поле должно быть заполнено")
    private LocalTime time;

}
