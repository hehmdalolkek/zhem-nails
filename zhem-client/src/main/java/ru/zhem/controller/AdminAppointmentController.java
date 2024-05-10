package ru.zhem.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.controller.util.ControllerUtil;
import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.dto.request.ZhemServiceDto;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.dto.response.AppointmentUpdateDto;
import ru.zhem.common.exceptions.CustomBindException;
import ru.zhem.service.interfaces.AppointmentService;
import ru.zhem.service.interfaces.IntervalService;
import ru.zhem.service.interfaces.ZhemServiceService;
import ru.zhem.service.interfaces.ZhemUserService;
import ru.zhem.service.util.CalendarUtil;

import java.time.DateTimeException;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    private final IntervalService intervalService;

    private final ZhemUserService zhemUserService;

    private final CalendarUtil calendarUtil;

    private final ControllerUtil controllerUtil;

    private final ZhemServiceService zhemServiceService;


    @GetMapping
    public String showAllAppointments(@RequestParam(value = "year", required = false) Integer year,
                                      @RequestParam(value = "month", required = false) Integer month, Model model,
                                      Locale locale) {
        YearMonth yearMonth = this.calendarUtil.calcPrevNextMonth(model, year, month);
        model.addAttribute("mapOfAppointments", this.controllerUtil.generateAppointmentCalendarForMonth(yearMonth));
        model.addAttribute("mapOfAppointmentsIsEmpty",
                this.appointmentService.findAllAppointments(year, month).isEmpty());
        String monthTitle = yearMonth.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, locale);
        monthTitle = monthTitle.replaceFirst(monthTitle.substring(0, 1), monthTitle.substring(0, 1).toUpperCase());
        model.addAttribute("monthTitle", monthTitle);
        return "admin/appointments/appointments";
    }

    @GetMapping("/interval/{intervalId:\\d+}")
    public String showAppointmentByInterval(@PathVariable("intervalId") long intervalId, Model model, Locale locale) {
        AppointmentDto appointment = this.appointmentService.findAppointmentByIntervalId(intervalId);
        model.addAttribute("appointment", appointment);
        model.addAttribute("monthTitle",
                appointment.getInterval().getDate().getMonth().getDisplayName(TextStyle.FULL, locale));
        return "admin/appointments/appointment";
    }


    @GetMapping("/create/step1")
    public String initCreateAppointmentPageFirst(@RequestParam("intervalId") long intervalId, Model model,
                                                 @RequestParam(value = "firstName", required = false) String firstName,
                                                 @RequestParam(value = "lastName", required = false) String lastName,
                                                 @RequestParam(value = "phone", required = false) String phone,
                                                 @RequestParam(value = "email", required = false) String email) {
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
        return "admin/appointments/create/create-step-1";
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
        return "admin/appointments/create/create-step-2";
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
            return "admin/appointments/create/create-step-2";
        }
        IntervalDto interval = this.intervalService.findIntervalById(appointment.getIntervalId());
        model.addAttribute("interval", interval);
        return "admin/appointments/create/create-step-3";
    }

    @PostMapping("/create/step4")
    public String createAppointmentProcessFourth(@Valid @ModelAttribute("appointment") AppointmentCreationDto appointment,
                                                 BindingResult bindingResult, HttpServletResponse response,
                                                 RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addFlashAttribute("message", "Клиент не записан");
        redirectAttributes.addFlashAttribute("messageType", "danger");
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = this.controllerUtil.getErrors(bindingResult);
            redirectAttributes.addFlashAttribute("errors", errors);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "redirect:/admin/intervals";
        } else {
            try {
                this.appointmentService.createAppointment(appointment);
                redirectAttributes.addFlashAttribute("message", "Клиент записан");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/admin/intervals";
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                Map<String, String> errors = exception.getErrors();
                if (errors.containsKey("details")) {
                    model.addAttribute("errors", errors);
                    IntervalDto interval = this.intervalService.findIntervalById(appointment.getIntervalId());
                    model.addAttribute("interval", interval);
                    return "admin/appointments/create/create-step-3";
                } else {
                    redirectAttributes.addFlashAttribute("errors", errors);
                    return "redirect:/admin/intervals";
                }
            }
        }
    }

    @GetMapping("/update/{appointmentId:\\d+}/step1")
    public String initUpdateAppointmentPageFirst(@PathVariable("appointmentId") long appointmentId, Model model,
                                                 @RequestParam(value = "firstName", required = false) String firstName,
                                                 @RequestParam(value = "lastName", required = false) String lastName,
                                                 @RequestParam(value = "phone", required = false) String phone,
                                                 @RequestParam(value = "email", required = false) String email) {
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
        return "admin/appointments/update/update-step-1";
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
                    this.controllerUtil.generateIntervalCalendarForMonth(yearMonth, true));
            model.addAttribute("appointmentId", appointmentId);
            return "admin/appointments/update/update-step-2";
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
            return "admin/appointments/update/update-step-3";
        }
    }

    @PostMapping("/update/{appointmentId:\\d+}/step4")
    public String updateAppointmentProcessFourth(@PathVariable("appointmentId") long appointmentId,
                                                 @Valid @ModelAttribute("appointment") AppointmentUpdateDto appointment,
                                                 BindingResult bindingResult, Model model,
                                                 HttpServletResponse response) {
        if (bindingResult.hasErrors() && bindingResult.getFieldError("services") != null) {
            Map<String, String> errors = this.controllerUtil.getErrors(bindingResult);
            model.addAttribute("errors", errors);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("allServices", this.zhemServiceService.findAllServices());
            model.addAttribute("appointmentId", appointmentId);
            return "admin/appointments/update/update-step-3";
        } else {
            model.addAttribute("appointmentId", appointmentId);
            return "admin/appointments/update/update-step-4";
        }
    }

    @PostMapping("/update/{appointmentId:\\d+}/step5")
    public String updateAppointmentProcessFifth(@PathVariable("appointmentId") long appointmentId,
                                                @Valid @ModelAttribute("appointment") AppointmentUpdateDto appointment,
                                                BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                                HttpServletResponse response, Model model) {
        if (bindingResult.hasErrors()) {
            return checkBindingResult(bindingResult, response, redirectAttributes, appointment.getIntervalId());
        } else {
            try {
                this.appointmentService.updateAppointment(appointmentId, appointment);
                redirectAttributes.addFlashAttribute("message", "Интервал успешно изменен");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/admin/appointments/interval/" + appointment.getIntervalId();
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                Map<String, String> errors = exception.getErrors();
                if (errors.containsKey("details")) {
                    model.addAttribute("errors", errors);
                    model.addAttribute("appointmentId", appointmentId);
                    return "admin/appointments/update/update-step-4";
                } else {
                    redirectAttributes.addFlashAttribute("message", "Клиент не записан");
                    redirectAttributes.addFlashAttribute("messageType", "danger");
                    redirectAttributes.addFlashAttribute("errors", errors);
                    return "redirect:/admin/intervals";
                }
            }
        }
    }

    @PostMapping("/delete")
    public String deleteAppointment(Long appointmentId, RedirectAttributes redirectAttributes) {
        this.appointmentService.deleteAppointment(appointmentId);
        redirectAttributes.addFlashAttribute("message", "Запись отменена");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/admin/intervals";
    }

    private String checkBindingResult(BindingResult bindingResult, HttpServletResponse response,
                                      RedirectAttributes redirectAttributes, long intervalId) {
        Map<String, String> errors = this.controllerUtil.getErrors(bindingResult);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        redirectAttributes.addFlashAttribute("errors", errors);
        redirectAttributes.addFlashAttribute("message", "Запись не изменена");
        redirectAttributes.addFlashAttribute("messageType", "danger");
        return "redirect:/admin/appointments/interval/" + intervalId;
    }
}
