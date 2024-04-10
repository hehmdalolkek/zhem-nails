package ru.zhem.dto.mapper;

import org.springframework.stereotype.Component;
import ru.zhem.dto.AppointmentCreationDto;
import ru.zhem.dto.AppointmentDto;
import ru.zhem.dto.AppointmentUpdateDto;
import ru.zhem.dto.DailyAppointmentDto;
import ru.zhem.entity.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class AppointmentMapper {

    public Appointment fromUpdateDto(AppointmentUpdateDto appointmentDto) {
        return Appointment.builder()
                .user(appointmentDto.getUser())
                .interval(appointmentDto.getInterval())
                .details(appointmentDto.getDetails())
                .build();
    }

    public Appointment fromCreationDto(AppointmentCreationDto appointmentDto) {
        return Appointment.builder()
                .user(appointmentDto.getUser())
                .interval(appointmentDto.getInterval())
                .details(appointmentDto.getDetails())
                .build();
    }

    public AppointmentDto fromEntity(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .user(appointment.getUser())
                .interval(appointment.getInterval())
                .details(appointment.getDetails())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }

    public DailyAppointmentDto fromEntryOfEntity(Map.Entry<LocalDate, List<Appointment>> appointmentsOfDate) {
        List<AppointmentDto> appointmentDtos = appointmentsOfDate.getValue().stream()
                .map(this::fromEntity)
                .toList();
        return DailyAppointmentDto.builder()
                .date(appointmentsOfDate.getKey())
                .appointments(appointmentDtos)
                .build();
    }

}
