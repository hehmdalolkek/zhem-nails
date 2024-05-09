package ru.zhem.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.DailyAppointmentDto;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.dto.response.AppointmentUpdateDto;

import java.util.List;

public interface AppointmentService {

    Page<AppointmentDto> findAllAppointmentsByUser(long userId, Pageable pageable);

    List<DailyAppointmentDto> findAllAppointments(Integer year, Integer month);

    AppointmentDto findAppointmentById(long appointmentId);

    void createAppointment(AppointmentCreationDto appointmentDto);

    void updateAppointment(long appointmentId, AppointmentUpdateDto appointmentDto);

    void deleteAppointment(long appointmentId);

    AppointmentDto findAppointmentByIntervalId(long intervalId);
}
