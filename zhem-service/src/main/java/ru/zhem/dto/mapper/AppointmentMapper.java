package ru.zhem.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.zhem.dto.request.AppointmentCreationDto;
import ru.zhem.dto.request.AppointmentUpdateDto;
import ru.zhem.dto.response.AppointmentDto;
import ru.zhem.dto.response.DailyAppointmentDto;
import ru.zhem.entity.Appointment;
import ru.zhem.entity.Interval;
import ru.zhem.entity.ZhemUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AppointmentMapper {

    private final UserMapper userMapper;

    private final IntervalMapper intervalMapper;

    private final ZhemServiceMapper zhemServiceMapper;

    public Appointment fromUpdateDto(AppointmentUpdateDto appointmentDto) {
        return Appointment.builder()
                .user(ZhemUser.builder().id(appointmentDto.getUserId()).build())
                .interval(Interval.builder().id(appointmentDto.getIntervalId()).build())
                .services(appointmentDto.getServices().stream()
                        .map(this.zhemServiceMapper::fromId)
                        .collect(Collectors.toSet()))
                .details(appointmentDto.getDetails())
                .build();
    }

    public Appointment fromCreationDto(AppointmentCreationDto appointmentDto) {
        return Appointment.builder()
                .user(ZhemUser.builder().id(appointmentDto.getUserId()).build())
                .interval(Interval.builder().id(appointmentDto.getIntervalId()).build())
                .services(appointmentDto.getServices().stream()
                        .map(this.zhemServiceMapper::fromId)
                        .collect(Collectors.toSet()))
                .details(appointmentDto.getDetails())
                .build();
    }

    public AppointmentDto fromEntity(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .user(this.userMapper.fromEntity(appointment.getUser()))
                .interval(this.intervalMapper.fromEntity(appointment.getInterval()))
                .services(appointment.getServices().stream()
                        .map(this.zhemServiceMapper::fromEntity)
                        .collect(Collectors.toSet()))
                .details(appointment.getDetails())
                .status(appointment.getStatus())
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
