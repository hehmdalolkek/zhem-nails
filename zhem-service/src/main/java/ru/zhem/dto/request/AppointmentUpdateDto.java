package ru.zhem.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdateDto {

    private Long userId;

    private Long intervalId;

    private Set<Integer> services;

    @Size(min = 2, max = 256, message = "Размер комментария должен быть в диапазоне от 2 до 256 символов")
    private String details;

}
