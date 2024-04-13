package ru.zhem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.entity.constraints.NullOrNotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreationDto {

    @NotNull(message = "Поле должно быть заполнено")
    private Long userId;

    @NotNull(message = "Поле должно быть заполнено")
    private Long intervalId;

    @NullOrNotBlank(message = "Поле должно содержать хотя бы 2 символа, либо быть полностью пустым")
    private String details;

}
