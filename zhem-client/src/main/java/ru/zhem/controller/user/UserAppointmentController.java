package ru.zhem.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.dto.request.ZhemServiceDto;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.exceptions.IntervalNotFoundException;
import ru.zhem.exceptions.NotFoundException;
import ru.zhem.service.AppointmentService;
import ru.zhem.service.IntervalService;
import ru.zhem.service.ZhemServiceService;
import ru.zhem.service.ZhemUserService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/appointments")
public class UserAppointmentController {

    private final AppointmentService appointmentService;

    private final ZhemUserService zhemUserService;

    private final IntervalService intervalService;

    private final ZhemServiceService zhemServiceService;

    @GetMapping
    public String findAllAppointmentsByUser(@RequestParam(value = "size", defaultValue = "7") int size,
                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                            HttpServletRequest request, Model model) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            ZhemUserDto user = zhemUserService.findUserByPhone(principal.getName());
            model.addAttribute("appointments",
                    this.appointmentService.findAllAppointmentsByUser(user.getId(), PageRequest.of(page, size)));
            model.addAttribute("dateNow", LocalDate.now());
            return "user/appointments/appointments";
        } else {
            return "redirect:/user/logout";
        }
    }

    @GetMapping("/create")
    public String initCreateAppointmentPageFirst(@RequestParam("intervalId") long intervalId, Model model) {
        try {
            IntervalDto interval = this.intervalService.findIntervalById(intervalId);
            AppointmentCreationDto appointment = AppointmentCreationDto.builder().intervalId(intervalId).build();
            List<ZhemServiceDto> services = this.zhemServiceService.findAllServices();

            model.addAttribute("interval", interval);
            model.addAttribute("appointment", appointment);
            model.addAttribute("services", services);
            return "/user/appointments/create";
        } catch (IntervalNotFoundException exception) {
            throw new NotFoundException(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()));
        }
    }

    @PostMapping("/create")
    public String createAppointmentProcess(@Valid AppointmentCreationDto appointmentDto, BindingResult bindingResult,
                                           RedirectAttributes redirectAttributes, HttpServletResponse response) {

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("errors", errors);
                redirectAttributes.addFlashAttribute("message", "Ошибка записи");
                return "redirect:/user/appointments/create?intervalId=" + appointmentDto.getIntervalId();
            } else {
                try {
                    this.appointmentService.createAppointment(appointmentDto);
                    return "redirect:/intervals";
                } catch (CustomBindException exception) {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    redirectAttributes.addFlashAttribute("message", "Ошибка записи");
                    redirectAttributes.addFlashAttribute("errors", exception.getErrors());
                    return "redirect:/intervals";
                }
            }
    }

    @PostMapping("/delete")
    public String deleteAppointment(Long appointmentId, RedirectAttributes redirectAttributes,
                                    HttpServletResponse response) {
        try {
            this.appointmentService.deleteAppointment(appointmentId);
            redirectAttributes.addFlashAttribute("message", "Запись отменена");
            return "redirect:/user/appointments";
        } catch (NotFoundException exception) {
            throw new NotFoundException(ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND, exception.getMessage()
            ));
        }
    }

}
