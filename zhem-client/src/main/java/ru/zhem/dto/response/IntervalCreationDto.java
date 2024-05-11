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

    @NotNull(message = "Дата должна быть указана")
    private LocalDate date;

    @NotNull(message = "Время должно быть указано")
    private LocalTime time;

}
