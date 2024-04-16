package ru.zhem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdateDto {

    private Long userId;

    private Long intervalId;

    private Set<Integer> services;

    private String details;

}
