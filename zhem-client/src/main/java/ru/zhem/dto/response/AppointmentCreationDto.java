package ru.zhem.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.dto.constraints.NullOrNotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreationDto {

    @NotNull(message = "Поле должно быть заполнено")
    private Long userId;

    @NotNull(message = "Поле должно быть заполнено")
    private Long intervalId;

    private String details;

}
