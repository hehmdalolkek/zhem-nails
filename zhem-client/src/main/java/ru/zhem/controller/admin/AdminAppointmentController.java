package ru.zhem.controller.admin;

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
import ru.zhem.dto.request.ZhemServiceDto;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.dto.response.AppointmentUpdateDto;
import ru.zhem.exceptions.*;
import ru.zhem.service.*;

import java.time.DateTimeException;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    private final IntervalService intervalService;

    private final ZhemUserService zhemUserService;

    private final CalendarService calendarService;

    private final ZhemServiceService zhemServiceService;


    @GetMapping
    public String showAllAppointments(@RequestParam(value = "year", required = false) Integer year, @RequestParam(value = "month", required = false) Integer month, Model model) {
        try {
            YearMonth yearMonth = this.calendarService.calcPrevNextMonth(model, year, month);
            model.addAttribute("mapOfAppointments", this.appointmentService.generateCalendarForMonth(yearMonth));
        } catch (InvalidDateException exception) {
            throw new BadRequestException(ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST, "Invalid date"
            ));
        }

        return "/admin/appointments/appointments";
    }

    @GetMapping("/interval/{intervalId:\\d+}")
    public String showAppointmentByInterval(@PathVariable("intervalId") long intervalId, Model model) {
        try {
            model.addAttribute("appointment", this.appointmentService.findAppointmentByIntervalId(intervalId));

            return "/admin/appointments/appointment";
        } catch (AppointmentNotFoundException exception) {
            throw new NotFoundException(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()));
        }
    }


    @GetMapping("/create/step1")
    public String initCreateAppointmentPageFirst(@RequestParam("intervalId") long intervalId, Model model,
                                                 @RequestParam(value = "firstName", required = false) String firstName,
                                                 @RequestParam(value = "lastName", required = false) String lastName,
                                                 @RequestParam(value = "phone", required = false) String phone,
                                                 @RequestParam(value = "email", required = false) String email) {
        try {
            IntervalDto interval = this.intervalService.findIntervalById(intervalId);
            AppointmentCreationDto appointment = AppointmentCreationDto.builder().intervalId(intervalId).build();
            List<ZhemUserDto> users;
            if (Objects.nonNull(firstName) || Objects.nonNull(lastName) || Objects.nonNull(phone) || Objects.nonNull(email)) {
                users = this.zhemUserService.findAllClientsBy(firstName, lastName, phone, email);
            } else {
                users = this.zhemUserService.findAllClients();
            }

            model.addAttribute("interval", interval);
            model.addAttribute("appointment", appointment);
            model.addAttribute("users", users);
            return "/admin/appointments/create/create-step-1";
        } catch (IntervalNotFoundException exception) {
            throw new NotFoundException(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()));
        }
    }

    @PostMapping("/create/step2")
    public String createAppointmentProcessSecond(@Valid @ModelAttribute("appointment") AppointmentCreationDto appointment,
                                                 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                                                 HttpServletResponse response) {
        if (bindingResult.hasErrors() && (bindingResult.getFieldError("userId") != null
                || bindingResult.getFieldError("intervalId") != null)) {
            Map<String, String> errors = new HashMap<>();
            FieldError userIdError = bindingResult.getFieldError("userId");
            if (userIdError != null) {
                errors.put(userIdError.getField(), userIdError.getDefaultMessage());
            }
            FieldError intervalIdError = bindingResult.getFieldError("intervalId");
            if (intervalIdError != null) {
                errors.put(intervalIdError.getField(), intervalIdError.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("errors", errors);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return intervalIdError != null ? "redirect:/admin/intervals"
                    : "redirect:/admin/appointments/create/step1?intervalId=" + appointment.getIntervalId();
        }
        IntervalDto interval = this.intervalService.findIntervalById(appointment.getIntervalId());
        model.addAttribute("interval", interval);
        List<ZhemServiceDto> services = this.zhemServiceService.findAllServices();
        model.addAttribute("services", services);
        return "/admin/appointments/create/create-step-2";
    }

    @PostMapping("/create/step3")
    public String createAppointmentProcessThird(@Valid @ModelAttribute("appointment") AppointmentCreationDto appointment,
                                                BindingResult bindingResult, Model model,
                                                HttpServletResponse response) {
        if (bindingResult.hasErrors() && bindingResult.getFieldError("services") != null) {
            FieldError servicesError = bindingResult.getFieldError("services");
            if (servicesError != null) {
                model.addAttribute("errors",
                        Map.of(servicesError.getField(), servicesError.getDefaultMessage()));
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            IntervalDto interval = this.intervalService.findIntervalById(appointment.getIntervalId());
            model.addAttribute("interval", interval);
            List<ZhemServiceDto> services = this.zhemServiceService.findAllServices();
            model.addAttribute("services", services);
            return "/admin/appointments/create/create-step-2";
        }
        try {
            IntervalDto interval = this.intervalService.findIntervalById(appointment.getIntervalId());
            model.addAttribute("interval", interval);
            return "/admin/appointments/create/create-step-3";
        } catch (IntervalNotFoundException exception) {
            throw new NotFoundException(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()));
        }
    }

    @PostMapping("/create/step4")
    public String createAppointmentProcessFourth(@Valid @ModelAttribute("appointment") AppointmentCreationDto appointment, BindingResult bindingResult, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("message", "Клиент не записан");
            return "redirect:/admin/intervals";
        } else {
            try {
                this.appointmentService.createAppointment(appointment);
                redirectAttributes.addFlashAttribute("message", "Клиент записан");
                return "redirect:/admin/intervals";
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("message", "Клиент не записан");
                redirectAttributes.addFlashAttribute("errors", exception.getErrors());
                return "redirect:/admin/intervals";
            }
        }
    }

    @GetMapping("/update/{appointmentId:\\d+}/step1")
    public String initUpdateAppointmentPageFirst(@PathVariable("appointmentId") long appointmentId, Model model,
                                                 @RequestParam(value = "firstName", required = false) String firstName,
                                                 @RequestParam(value = "lastName", required = false) String lastName,
                                                 @RequestParam(value = "phone", required = false) String phone,
                                                 @RequestParam(value = "email", required = false) String email) {
        try {
            AppointmentDto appointmentDto = this.appointmentService.findAppointmentById(appointmentId);
            AppointmentUpdateDto appointment = AppointmentUpdateDto.builder()
                    .intervalId(appointmentDto.getInterval().getId())
                    .userId(appointmentDto.getUser().getId())
                    .services(appointmentDto.getServices().stream()
                            .map(ZhemServiceDto::getId)
                            .collect(Collectors.toSet()))
                    .details(appointmentDto.getDetails()).build();
            List<ZhemUserDto> users;
            if (Objects.nonNull(firstName) || Objects.nonNull(lastName)
                    || Objects.nonNull(phone) || Objects.nonNull(email)) {
                users = this.zhemUserService.findAllClientsBy(firstName, lastName, phone, email);
            } else {
                users = this.zhemUserService.findAllClients();
                ZhemUserDto user = users.stream()
                        .filter(u -> u.getId().equals(appointment.getUserId()))
                        .findFirst()
                        .orElse(null);
                users.remove(user);
                users.addFirst(user);
            }
            model.addAttribute("appointmentId", appointmentId);
            model.addAttribute("appointment", appointment);
            model.addAttribute("users", users);
        } catch (AppointmentNotFoundException exception) {
            throw new NotFoundException(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()));
        }
        return "/admin/appointments/update/update-step-1";
    }

    @PostMapping("/update/{appointmentId:\\d+}/step2")
    public String updateAppointmentProcessSecond(@PathVariable("appointmentId") long appointmentId,
                                                 @RequestParam(value = "year", required = false) String year,
                                                 @RequestParam(value = "month", required = false) Integer month,
                                                 @Valid @ModelAttribute("appointment") AppointmentUpdateDto appointment,
                                                 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                                                 HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return checkBindingResult(bindingResult, response, redirectAttributes, appointment.getIntervalId());
        } else {
            YearMonth yearMonth;
            try {
                yearMonth = YearMonth.of(Integer.parseInt(year), month);
            } catch (NumberFormatException | DateTimeException exception) {
                yearMonth = YearMonth.now();
            }
            model.addAttribute("interval", this.intervalService.findIntervalById(appointment.getIntervalId()));
            model.addAttribute("mapOfIntervals",
                    this.intervalService.generateIntervalCalendarForMonth(yearMonth, true));
            model.addAttribute("appointmentId", appointmentId);
            return "/admin/appointments/update/update-step-2";
        }
    }

    @PostMapping("/update/{appointmentId:\\d+}/step3")
    public String updateAppointmentProcessThird(@PathVariable("appointmentId") long appointmentId,
                                                @Valid @ModelAttribute("appointment") AppointmentUpdateDto appointment,
                                                BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes,
                                                HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return checkBindingResult(bindingResult, response, redirectAttributes, appointment.getIntervalId());
        } else {
            model.addAttribute("allServices", this.zhemServiceService.findAllServices());
            model.addAttribute("appointmentId", appointmentId);
            return "/admin/appointments/update/update-step-3";
        }
    }

    @PostMapping("/update/{appointmentId:\\d+}/step4")
    public String updateAppointmentProcessFourth(@PathVariable("appointmentId") long appointmentId,
                                                 @Valid @ModelAttribute("appointment") AppointmentUpdateDto appointment,
                                                 BindingResult bindingResult, Model model,
                                                 HttpServletResponse response) {
        if (bindingResult.hasErrors() && bindingResult.getFieldError("services") != null) {
            FieldError servicesError = bindingResult.getFieldError("services");
            if (servicesError != null) {
                model.addAttribute("errors",
                        Map.of(servicesError.getField(), servicesError.getDefaultMessage()));
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("allServices", this.zhemServiceService.findAllServices());
            model.addAttribute("appointmentId", appointmentId);
            return "/admin/appointments/update/update-step-3";
        } else {
            model.addAttribute("appointmentId", appointmentId);
            return "/admin/appointments/update/update-step-4";
        }
    }

    @PostMapping("/update/{appointmentId:\\d+}/step5")
    public String updateAppointmentProcessFifth(@PathVariable("appointmentId") long appointmentId,
                                                @Valid @ModelAttribute("appointment") AppointmentUpdateDto appointment,
                                                BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                                HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return checkBindingResult(bindingResult, response, redirectAttributes, appointment.getIntervalId());
        } else {
            try {
                this.appointmentService.updateAppointment(appointmentId, appointment);
                redirectAttributes.addFlashAttribute("message", "Интервал успешно изменен");
                return "redirect:/admin/appointments/interval/" + appointment.getIntervalId();
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("errors", exception.getErrors());
                redirectAttributes.addFlashAttribute("message", "Интервал не изменен");
                return "redirect:/admin/appointments/interval/" + appointment.getIntervalId();
            }
        }
    }

    private String checkBindingResult(BindingResult bindingResult, HttpServletResponse response,
                                      RedirectAttributes redirectAttributes, long intervalId) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        redirectAttributes.addFlashAttribute("errors", errors);
        redirectAttributes.addFlashAttribute("message", "Интервал не изменен");
        return "redirect:/admin/appointments/interval/" + intervalId;
    }

    @PostMapping("/delete")
    public String deleteAppointment(Long appointmentId, RedirectAttributes redirectAttributes) {
        this.appointmentService.deleteAppointment(appointmentId);
        redirectAttributes.addFlashAttribute("message", "Запись отменена");
        return "redirect:/admin/intervals";
    }
}
