package ru.zhem.dto.response;

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

    @NotNull(message = "Поле должно быть заполнено")
    private Long userId;

    @NotNull(message = "Поле должно быть заполнено")
    private Long intervalId;

    @NotNull(message = "Должна быть выбрана хотя бы одна услуга")
    private Set<Integer> services;

    private String details;

}
