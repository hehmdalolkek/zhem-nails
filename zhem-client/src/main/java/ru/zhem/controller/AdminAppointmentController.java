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
import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.dto.response.AppointmentUpdateDto;
import ru.zhem.exceptions.AppointmentNotFoundException;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.exceptions.IntervalNotFoundException;
import ru.zhem.exceptions.NotFoundException;
import ru.zhem.service.AppointmentService;
import ru.zhem.service.CalendarService;
import ru.zhem.service.IntervalService;
import ru.zhem.service.ZhemUserService;

import java.time.DateTimeException;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

            return "/admin/appointments/appointment";
        } catch (AppointmentNotFoundException exception) {
            throw new NotFoundException(
                    ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()));
        }
    }


    @GetMapping("/create/step1")
    public String initCreateAppointmentPageFirst(@RequestParam("intervalId") long intervalId,
                                                 Model model, HttpServletRequest request,
                                                 @RequestParam(value = "firstName", required = false) String firstName,
                                                 @RequestParam(value = "lastName", required = false) String lastName,
                                                 @RequestParam(value = "phone", required = false) String phone,
                                                 @RequestParam(value = "email", required = false) String email) {
        try {
            IntervalDto interval = this.intervalService.findIntervalById(intervalId);
            AppointmentCreationDto appointment = AppointmentCreationDto.builder()
                    .intervalId(intervalId)
                    .build();
            List<ZhemUserDto> users;
            if (Objects.nonNull(firstName) || Objects.nonNull(lastName)
                    || Objects.nonNull(phone) || Objects.nonNull(email)) {
                users = this.zhemUserService.findAllUsersBy(firstName, lastName, phone, email);
            } else {
                users = this.zhemUserService.findAllUsers(null);
            }

            model.addAttribute("interval", interval);
            model.addAttribute("appointment", appointment);
            model.addAttribute("users", users);
            model.addAttribute("referer", request.getHeader("Referer"));
            return "/admin/appointments/create/create-step-1";
        } catch (IntervalNotFoundException exception) {
            throw new NotFoundException(
                    ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()));
        }
    }

    @PostMapping("/create/step2")
    public String createAppointmentProcessSecond(@Valid @ModelAttribute("appointment") AppointmentCreationDto appointment,
                                                 BindingResult bindingResult, HttpServletResponse response,
                                                 RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/admin/appointments/create/step1?intervalId=" + appointment.getIntervalId();
        } else {
            try {
                IntervalDto interval = this.intervalService.findIntervalById( appointment.getIntervalId());
                model.addAttribute("interval", interval);
                return "/admin/appointments/create/create-step-2";
            } catch (IntervalNotFoundException exception) {
                throw new NotFoundException(
                        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()));
            }
        }
    }

    @PostMapping("/create/step3")
    public String createAppointmentProcessThird(@Valid @ModelAttribute("appointment") AppointmentCreationDto appointment,
                                                 BindingResult bindingResult, HttpServletResponse response,
                                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/admin/appointments/create/step1?intervalId=" + appointment.getIntervalId();
        } else {
            try {
                redirectAttributes.addFlashAttribute("message", "Клиент записан");
                this.appointmentService.createAppointment(appointment);
                return "redirect:/admin/intervals";
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("errors", exception.getErrors());
                return "redirect:/admin/intervals";
            }
        }
    }

    @GetMapping("/update/{appointmentId:\\d+}/step1")
    public String initUpdateAppointmentPageFirst(@PathVariable("appointmentId") long appointmentId,
                                                 Model model, HttpServletRequest request,
                                                 @RequestParam(value = "firstName", required = false) String firstName,
                                                 @RequestParam(value = "lastName", required = false) String lastName,
                                                 @RequestParam(value = "phone", required = false) String phone,
                                                 @RequestParam(value = "email", required = false) String email) {
        try {
            AppointmentDto appointmentDto = this.appointmentService.findAppointmentById(appointmentId);
            AppointmentUpdateDto appointment = AppointmentUpdateDto.builder()
                    .intervalId(appointmentDto.getInterval().getId())
                    .userId(appointmentDto.getUser().getId())
                    .details(appointmentDto.getDetails())
                    .build();

            List<ZhemUserDto> users;
            if (Objects.nonNull(firstName) || Objects.nonNull(lastName)
                    || Objects.nonNull(phone) || Objects.nonNull(email)) {
                users = this.zhemUserService.findAllUsersBy(firstName, lastName, phone, email);
            } else {
                users = this.zhemUserService.findAllUsers(null);
                ZhemUserDto user = users.stream()
                        .filter(u -> u.getId().equals(appointmentDto.getUser().getId()))
                        .findFirst()
                        .orElse(null);
                users.remove(user);
                users.set(0, user);
            }

            model.addAttribute("appointmentId", appointmentId);
            model.addAttribute("appointment", appointment);
            model.addAttribute("users", users);
            model.addAttribute("referer", request.getHeader("Referer"));
        } catch (AppointmentNotFoundException exception) {
            throw new NotFoundException(
                    ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()));
        }
        return "/admin/appointments/update/update-step-1";
    }

    @PostMapping("/update/{appointmentId:\\d+}/step2")
    public String updateAppointmentProcessSecond(@PathVariable("appointmentId") long appointmentId,
                                                 @RequestParam(value = "year", required = false) String year,
                                                 @RequestParam(value = "month", required = false) Integer month,
                                                 @ModelAttribute("appointment") AppointmentUpdateDto appointment,
                                                 Model model, HttpServletRequest request) {
        YearMonth yearMonth;
        try {
            yearMonth = YearMonth.of(Integer.parseInt(year), month);
        } catch (NumberFormatException | DateTimeException exception) {
            yearMonth = YearMonth.now();
        }
        model.addAttribute("interval",
                this.intervalService.findIntervalById(appointment.getIntervalId()));
        model.addAttribute("mapOfIntervals",
                this.intervalService.generateIntervalCalendarForMonth(yearMonth, true));
        model.addAttribute("appointmentId", appointmentId);
        model.addAttribute("referer", request.getHeader("Referer"));
        return "/admin/appointments/update/update-step-2";
    }

    @PostMapping("/update/{appointmentId:\\d+}/step3")
    public String updateAppointmentProcessThird(@PathVariable("appointmentId") long appointmentId,
                                                @ModelAttribute("appointment") AppointmentUpdateDto appointment,
                                                Model model, HttpServletRequest request) {
        model.addAttribute("appointmentId", appointmentId);
        model.addAttribute("referer", request.getHeader("Referer"));
        return "/admin/appointments/update/update-step-3";
    }

    @PostMapping("/update/{appointmentId:\\d+}/step4")
    public String updateAppointmentProcessFourth(@PathVariable("appointmentId") long appointmentId,
                                                 @ModelAttribute("appointment") AppointmentUpdateDto appointment,
                                                 RedirectAttributes redirectAttributes,
                                                 HttpServletResponse response) {
        try {
            this.appointmentService.updateAppointment(appointmentId, appointment);
            redirectAttributes.addFlashAttribute("message", "Интервал успешно изменен");
            return "redirect:/admin/appointments/interval/" + appointment.getIntervalId();
        } catch (CustomBindException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("errors", exception.getErrors());
            return "redirect:/admin/appointments/interval/" + appointment.getIntervalId();
        }
    }

    @PostMapping("/delete")
    public String deleteAppointment(Long appointmentId, RedirectAttributes redirectAttributes) {
        this.appointmentService.deleteAppointment(appointmentId);
        redirectAttributes.addFlashAttribute("message", "Запись отменена");
        return "redirect:/admin/intervals";
    }
}
