package ru.zhem.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.dto.request.constraint.NullOrNotBlank;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreationDto {

    @NotNull(message = "Поле должно быть заполнено")
    private Long userId;

    @NotNull(message = "Поле должно быть заполнено")
    private Long intervalId;

    @NotNull(message = "Должна быть выбрана хотя бы одна услуга")
    @NotEmpty(message = "Должна быть выбрана хотя бы одна услуга")
    private Set<Integer> services;

    @NullOrNotBlank(message = "Поле должно содержать хотя бы 2 символа, либо быть полностью пустым")
    @Size(min = 2, max = 256, message = "Размер поля должен быть в диапазоне от 2 до 256 символов")
    private String details;

}
