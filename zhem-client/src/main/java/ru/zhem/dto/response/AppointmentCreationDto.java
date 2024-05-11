package ru.zhem.dto.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreationDto {

    @NotNull(message = "ID пользователя должно быть указано")
    private Long userId;

    @NotNull(message = "ID интервала должно быть указано")
    private Long intervalId;

    @NotNull(message = "Должна быть выбрана хотя бы одна услуга")
    @NotEmpty(message = "Должна быть выбрана хотя бы одна услуга")
    private Set<Integer> services;

    private String details;

}
