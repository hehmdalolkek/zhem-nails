package ru.zhem.service;

import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.DailyAppointmentDto;
import ru.zhem.dto.response.AppointmentCreationDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface AppointmentService {

    List<DailyAppointmentDto> findAllAppointmentsByUser(long userId);

    List<DailyAppointmentDto> findAllAppointments(int year, int month);

    AppointmentDto findAppointmentById(long appointmentId);

    void createAppointment(AppointmentCreationDto appointmentDto);

    void updateAppointment(long appointmentId, AppointmentCreationDto appointmentDto);

    void deleteAppointment(long appointmentId);

    Map<LocalDate, List<AppointmentDto>> generateCalendarForMonth(YearMonth yearMonth);

}
