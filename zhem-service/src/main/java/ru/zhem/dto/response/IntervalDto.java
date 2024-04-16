package ru.zhem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.entity.IntervalStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
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
