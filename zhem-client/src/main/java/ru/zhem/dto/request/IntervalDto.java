package ru.zhem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.entity.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntervalDto {

    private Long id;

    private LocalDate date;

    private LocalTime time;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public String getFormattedDate() {
        return this.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

}
