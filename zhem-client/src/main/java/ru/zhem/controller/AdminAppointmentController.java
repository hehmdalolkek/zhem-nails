package ru.zhem.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.client.AppointmentRestClient;
import ru.zhem.client.IntervalRestClient;
import ru.zhem.client.ZhemUserRestClient;
import ru.zhem.client.response.PaginatedResponse;
import ru.zhem.dto.request.*;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.entity.Status;
import ru.zhem.exceptions.IntervalNotFoundException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

    private final AppointmentRestClient appointmentRestClient;

    private final IntervalRestClient intervalRestClient;

    private final ZhemUserRestClient zhemUserRestClient;

    @GetMapping("/create")
    public String initCreateAppointmentPage(@RequestParam("intervalId") long intervalId, Model model,
                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "7") int size) {
        IntervalDto interval = this.intervalRestClient.findIntervalById(intervalId)
                .orElseThrow(() -> new IntervalNotFoundException("Interval not found"));
        PaginatedResponse<ZhemUserDto> users = this.zhemUserRestClient.findAllUsersByPage(page, size);
        model.addAttribute("interval", interval);
        model.addAttribute("users", users);
        return "/admin/appointments/create";
    }

    @PostMapping("/create")
    public String createAppointment(@RequestParam("intervalId") long intervalId, @Valid AppointmentCreationDto appointmentDto,
                                    BindingResult bindingResult, HttpServletResponse response,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("enteredData", appointmentDto);
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/admin/appointments/create?intervalId=" + intervalId;
        } else {
            this.appointmentRestClient.createAppointment(appointmentDto);
            redirectAttributes.addFlashAttribute("recorded", true);
            return "redirect:/admin/intervals";
        }
    }

    @GetMapping
    public String showAllAppointments(@RequestParam(value = "year", required = false) Integer year,
                                   @RequestParam(value = "month", required = false) Integer month, Model model) {
        if (Objects.isNull(year) || Objects.isNull(month)) {
            year = LocalDate.now().getYear();
            month = LocalDate.now().getMonthValue();
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        YearMonth prevYearMonth = yearMonth.minusMonths(1);
        YearMonth nextYearMonth = yearMonth.plusMonths(1);
        model.addAttribute("prevYearMonth", prevYearMonth);
        model.addAttribute("nextYearMonth", nextYearMonth);
        model.addAttribute("mapOfAppointments", this.generateCalendarForMonth(yearMonth));

        return "/admin/appointments/appointments";
    }

    private Map<LocalDate, List<AppointmentDto>> generateCalendarForMonth(YearMonth yearMonth) {
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
}
