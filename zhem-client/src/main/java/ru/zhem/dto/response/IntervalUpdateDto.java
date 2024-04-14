package ru.zhem.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.entity.Status;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntervalUpdateDto {

    private LocalDate date;

    @NotNull(message = "Время должно быть заполнено")
    private LocalTime time;

    private Status status;

}
