package ru.zhem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.entity.Interval;
import ru.zhem.entity.ZhemUser;
import ru.zhem.entity.constraints.NullOrNotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdateDto {

    private ZhemUser user;

    private Interval interval;

    @NullOrNotBlank(message = "Поле должно иметь хотя бы один не пустой символ")
    private String details;

}
