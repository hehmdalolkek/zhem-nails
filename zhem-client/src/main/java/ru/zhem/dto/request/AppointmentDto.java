package ru.zhem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {

    private Long id;

    private ZhemUserDto user;

    private IntervalDto interval;

    private Set<ZhemServiceDto> services;

    private String details;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public String getFormattedCreatedAt() {
        return this.createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

}
