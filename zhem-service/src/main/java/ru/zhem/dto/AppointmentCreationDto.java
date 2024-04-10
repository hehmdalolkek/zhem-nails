package ru.zhem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.entity.Interval;
import ru.zhem.entity.ZhemUser;
import ru.zhem.entity.constraints.NullOrNotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreationDto {

    @NotNull(message = "Поле должно быть заполнено")
    private ZhemUser user;

    @NotNull(message = "Поле должно быть заполнено")
    private Interval interval;

    @NullOrNotBlank(message = "Поле должно иметь хотя бы один не пустой символ")
    private String details;

}
