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

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime time;

}
