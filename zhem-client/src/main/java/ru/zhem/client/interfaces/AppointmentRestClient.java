package ru.zhem.client.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.DailyAppointmentDto;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.dto.response.AppointmentUpdateDto;

import java.util.List;
import java.util.Optional;

public interface AppointmentRestClient {

    Page<AppointmentDto> findAllAppointmentsByUser(long userId, Pageable pageable);

    List<DailyAppointmentDto> findAllAppointments(int year, int month);

    Optional<AppointmentDto> findAppointmentById(long appointmentId);

    void createAppointment(AppointmentCreationDto appointmentDto);

    void updateAppointment(long appointmentId, AppointmentUpdateDto appointmentDto);

    void deleteAppointment(long appointmentId);

    Optional<AppointmentDto> findAppointmentByInterval(long intervalId);
}
