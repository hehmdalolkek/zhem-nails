package ru.zhem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.zhem.client.response.PaginatedResponse;
import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.DailyAppointmentDto;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.dto.response.AppointmentUpdateDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface AppointmentService {

    Page<AppointmentDto> findAllAppointmentsByUser(long userId, Pageable pageable);

    List<DailyAppointmentDto> findAllAppointments(int year, int month);

    AppointmentDto findAppointmentById(long appointmentId);

    void createAppointment(AppointmentCreationDto appointmentDto);

    void updateAppointment(long appointmentId, AppointmentUpdateDto appointmentDto);

    void deleteAppointment(long appointmentId);

    Map<LocalDate, List<AppointmentDto>> generateCalendarForMonth(YearMonth yearMonth);

    AppointmentDto findAppointmentByIntervalId(long intervalId);
}
