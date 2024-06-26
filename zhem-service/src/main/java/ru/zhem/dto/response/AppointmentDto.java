package ru.zhem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {

    private Long id;

    private ZhemUserDto user;

    private IntervalDto interval;

    private String details;

    private Set<ZhemServiceDto> services;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
