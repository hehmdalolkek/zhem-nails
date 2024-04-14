package ru.zhem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.client.AppointmentRestClient;
import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.DailyAppointmentDto;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.exceptions.AppointmentNotFoundException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRestClient appointmentRestClient;

    @Override
    public List<DailyAppointmentDto> findAllAppointmentsByUser(long userId) {
        return null;
    }

    @Override
    public List<DailyAppointmentDto> findAllAppointments(int year, int month) {
        return null;
    }

    @Override
    public AppointmentDto findAppointmentById(long appointmentId) {
        return null;
    }

    @Override
    public void createAppointment(AppointmentCreationDto appointmentDto) {
        if (appointmentDto.getDetails().isBlank()) {
            appointmentDto.setDetails(null);
        }
        this.appointmentRestClient.createAppointment(appointmentDto);
    }

    @Override
    public void updateAppointment(long appointmentId, AppointmentCreationDto appointmentDto) {

    }

    @Override
    public void deleteAppointment(long appointmentId) {

    }

    @Override
    public Map<LocalDate, List<AppointmentDto>> generateCalendarForMonth(YearMonth yearMonth) {
        Map<LocalDate, List<AppointmentDto>> mapOfAppointments = new LinkedHashMap<>();
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            mapOfAppointments.put(date, new ArrayList<>());
        }

        List<DailyAppointmentDto> dailyAppointments =
                this.appointmentRestClient.findAllAppointments(yearMonth.getYear(), yearMonth.getMonthValue());
        for (DailyAppointmentDto dailyAppointment : dailyAppointments) {
            mapOfAppointments.put(dailyAppointment.getDate(), dailyAppointment.getAppointments());
        }

        return mapOfAppointments;
    }

    @Override
    public AppointmentDto findAppointmentByIntervalId(long intervalId) {
        return this.appointmentRestClient.findAppointmentByInterval(intervalId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
    }
}
