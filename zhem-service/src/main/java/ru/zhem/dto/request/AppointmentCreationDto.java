package ru.zhem.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.common.constraint.NullOrNotBlank;

import java.util.Set;

@Data
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

    @NullOrNotBlank(message = "Комментарий должен содержать хотя бы 2 символа, либо быть полностью пустым")
    @Size(min = 2, max = 256, message = "Размер комментария должен быть в диапазоне от 2 до 256 символов")
    private String details;

}
