package ru.zhem.client;

import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.DailyAppointmentDto;
import ru.zhem.dto.response.AppointmentCreationDto;

import java.util.List;
import java.util.Optional;

public interface AppointmentRestClient {

    List<DailyAppointmentDto> findAllAppointmentsByUser(long userId);

    List<DailyAppointmentDto> findAllAppointments(int year, int month);

    Optional<AppointmentDto> findAppointmentById(long appointmentId);

    void createAppointment(AppointmentCreationDto appointmentDto);

    void updateAppointment(long appointmentId, AppointmentCreationDto appointmentDto);

    void deleteAppointment(long appointmentId);

}
