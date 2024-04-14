package ru.zhem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.client.response.PaginatedResponse;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.exceptions.AppointmentNotFoundException;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.exceptions.IntervalNotFoundException;
import ru.zhem.exceptions.NotFoundException;
import ru.zhem.service.AppointmentService;
import ru.zhem.service.CalendarService;
import ru.zhem.service.IntervalService;
import ru.zhem.service.ZhemUserService;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    private final IntervalService intervalService;

    private final ZhemUserService zhemUserService;

    private final CalendarService calendarService;


    @GetMapping
    public String showAllAppointments(@RequestParam(value = "year", required = false) Integer year,
                                      @RequestParam(value = "month", required = false) Integer month, Model model) {
        YearMonth yearMonth = this.calendarService.calcPrevNextMonth(model, year, month);
        model.addAttribute("mapOfAppointments",
                this.appointmentService.generateCalendarForMonth(yearMonth));

        return "/admin/appointments/appointments";
    }

    @GetMapping("/interval/{intervalId:\\d+}")
    public String showAppointmentByInterval(@PathVariable("intervalId") long intervalId, Model model,
                                            HttpServletRequest request) {
        try {
            model.addAttribute("appointment",
                    this.appointmentService.findAppointmentByIntervalId(intervalId));
            model.addAttribute("referer", request.getHeader("Referer"));

            return "/admin/appointments/appointment";
        } catch (AppointmentNotFoundException exception) {
            throw new NotFoundException(
                    ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()));
        }
    }


    @GetMapping("/create")
    public String initCreateAppointmentPage(@RequestParam("intervalId") long intervalId, Model model,
                                            HttpServletRequest request,
                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "7") int size) {
        try {
            IntervalDto interval = this.intervalService.findIntervalById(intervalId);
            PaginatedResponse<ZhemUserDto> users = this.zhemUserService.findAllUsersByPage(page, size);
            model.addAttribute("referer", request.getHeader("Referer"));
            model.addAttribute("interval", interval);
            model.addAttribute("users", users);
            return "/admin/appointments/create";
        } catch (IntervalNotFoundException exception) {
            throw new NotFoundException(
                    ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()));
        }
    }

    @PostMapping("/create")
    public String createAppointment(@RequestParam("intervalId") long intervalId, @Valid AppointmentCreationDto appointmentDto,
                                    BindingResult bindingResult, HttpServletResponse response,
                                    RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Клиент не записан");
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
            try {
                this.appointmentService.createAppointment(appointmentDto);
                redirectAttributes.addFlashAttribute("message", "Клиент записан");
                return "redirect:/admin/intervals";
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("errors", exception.getErrors());
                return "redirect:/admin/intervals";
            }
        }
    }

    @PostMapping("/delete")
    public String deleteAppointment(Long appointmentId, RedirectAttributes redirectAttributes) {
        this.appointmentService.deleteAppointment(appointmentId);
        redirectAttributes.addFlashAttribute("message", "Запись отменена");
        return "redirect:/admin/intervals";
    }
}
