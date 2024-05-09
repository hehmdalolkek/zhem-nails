package ru.zhem.controller.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.DailyAppointmentDto;
import ru.zhem.dto.request.DailyIntervalsDto;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.service.interfaces.AppointmentService;
import ru.zhem.service.interfaces.IntervalService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RequiredArgsConstructor
@Component
public class ControllerUtil {

    private final AppointmentService appointmentService;

    private final IntervalService intervalService;

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

        int firstDayOfWeek = startOfMonth.getDayOfWeek().getValue();
        YearMonth prevMonth = yearMonth.minusMonths(1);
        for (int i = firstDayOfWeek - 2; i >= 0; i--) {
            mapOfAppointments.put(prevMonth.atDay(prevMonth.lengthOfMonth() - i), new ArrayList<>());
        }

        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            mapOfAppointments.put(date, new ArrayList<>());
        }

        List<DailyAppointmentDto> dailyAppointments =
                this.appointmentService.findAllAppointments(yearMonth.getYear(), yearMonth.getMonthValue());
        for (DailyAppointmentDto dailyAppointment : dailyAppointments) {
            mapOfAppointments.put(dailyAppointment.getDate(), dailyAppointment.getAppointments());
        }

        int lastDayOfWeek = endOfMonth.getDayOfWeek().getValue();
        YearMonth nextMonth = yearMonth.plusMonths(1);
        for (int i = 1; i <= 7 - lastDayOfWeek; i++) {
            mapOfAppointments.put(nextMonth.atDay(i), new ArrayList<>());
        }

        return mapOfAppointments;
    }

    public Map<LocalDate, List<IntervalDto>> generateIntervalCalendarForMonth(YearMonth yearMonth,
                                                                              boolean isAvailable) {
        Map<LocalDate, List<IntervalDto>> mapOfIntervals = new LinkedHashMap<>();
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        int firstDayOfWeek = startOfMonth.getDayOfWeek().getValue();
        YearMonth prevMonth = yearMonth.minusMonths(1);
        for (int i = firstDayOfWeek - 2; i >= 0; i--) {
            mapOfIntervals.put(prevMonth.atDay(prevMonth.lengthOfMonth() - i), new ArrayList<>());
        }

        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            mapOfIntervals.put(date, new ArrayList<>());
        }

        List<DailyIntervalsDto> dailyIntervals;
        if (isAvailable) {
            dailyIntervals = this.intervalService.findAllAvailableIntervals(yearMonth.getYear(), yearMonth.getMonthValue());
        } else {
            dailyIntervals = this.intervalService.findAllIntervals(yearMonth.getYear(), yearMonth.getMonthValue());
        }

        for (DailyIntervalsDto dailyInterval : dailyIntervals) {
            mapOfIntervals.put(dailyInterval.getDate(), dailyInterval.getIntervals());
        }

        int lastDayOfWeek = endOfMonth.getDayOfWeek().getValue();
        YearMonth nextMonth = yearMonth.plusMonths(1);
        for (int i = 1; i <= 7 - lastDayOfWeek; i++) {
            mapOfIntervals.put(nextMonth.atDay(i), new ArrayList<>());
        }

        return mapOfIntervals;
    }

}
