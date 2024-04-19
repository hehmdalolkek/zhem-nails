package ru.zhem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zhem.client.AppointmentRestClient;
import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.DailyAppointmentDto;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.dto.response.AppointmentUpdateDto;
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
    public Page<AppointmentDto> findAllAppointmentsByUser(long userId, Pageable pageable) {
        return this.appointmentRestClient.findAllAppointmentsByUser(userId, pageable);
    }

    @Override
    public List<DailyAppointmentDto> findAllAppointments(int year, int month) {
        return null;
    }

    @Override
    public AppointmentDto findAppointmentById(long appointmentId) {
        return this.appointmentRestClient.findAppointmentById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
    }

    @Override
    public void createAppointment(AppointmentCreationDto appointmentDto) {
        if (appointmentDto.getDetails().isBlank()) {
            appointmentDto.setDetails(null);
        }
        this.appointmentRestClient.createAppointment(appointmentDto);
    }

    @Override
    public void updateAppointment(long appointmentId, AppointmentUpdateDto appointmentDto) {
        if (appointmentDto.getDetails().isBlank()) {
            appointmentDto.setDetails(null);
        }
        this.appointmentRestClient.updateAppointment(appointmentId, appointmentDto);
    }

    @Override
    public void deleteAppointment(long appointmentId) {
        this.appointmentRestClient.deleteAppointment(appointmentId);
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
