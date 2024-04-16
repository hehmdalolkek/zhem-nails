package ru.zhem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.entity.IntervalStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntervalDto {

    private Long id;

    private LocalDate date;

    private LocalTime time;

    private IntervalStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
