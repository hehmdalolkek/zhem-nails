package ru.zhem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyAppointmentDto {

    private LocalDate date;

    private List<AppointmentDto> appointments;

}
