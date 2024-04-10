package ru.zhem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.entity.Interval;
import ru.zhem.entity.ZhemUser;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {

    private Long id;

    private ZhemUser user;

    private Interval interval;

    private String details;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
