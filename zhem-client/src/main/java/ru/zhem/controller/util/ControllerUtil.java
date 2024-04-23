package ru.zhem.controller.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.zhem.client.interfaces.AppointmentRestClient;
import ru.zhem.client.interfaces.IntervalRestClient;
import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.DailyAppointmentDto;
import ru.zhem.dto.request.DailyIntervalsDto;
import ru.zhem.dto.request.IntervalDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RequiredArgsConstructor
@Component
public class ControllerUtil {

    private final AppointmentRestClient appointmentRestClient;

    private final IntervalRestClient intervalRestClient;

    public Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

    public Map<LocalDate, List<AppointmentDto>> generateAppointmentCalendarForMonth(YearMonth yearMonth) {
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

    public Map<LocalDate, List<IntervalDto>> generateIntervalCalendarForMonth(YearMonth yearMonth,
                                                                              boolean isAvailable) {
        Map<LocalDate, List<IntervalDto>> mapOfIntervals = new LinkedHashMap<>();
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            mapOfIntervals.put(date, new ArrayList<>());
        }

        List<DailyIntervalsDto> dailyIntervals;
        if (isAvailable) {
            dailyIntervals = this.intervalRestClient.findAllAvailableIntervals(yearMonth.getYear(), yearMonth.getMonthValue());
        } else {
            dailyIntervals = this.intervalRestClient.findAllIntervals(yearMonth.getYear(), yearMonth.getMonthValue());
        }

        for (DailyIntervalsDto dailyInterval : dailyIntervals) {
            mapOfIntervals.put(dailyInterval.getDate(), dailyInterval.getIntervals());
        }

        return mapOfIntervals;
    }

}
