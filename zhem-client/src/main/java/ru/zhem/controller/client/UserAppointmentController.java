package ru.zhem.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.service.AppointmentService;
import ru.zhem.service.ZhemUserService;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/appointments")
public class UserAppointmentController {

    private final AppointmentService appointmentService;

    private final ZhemUserService zhemUserService;

    @GetMapping
    public String findAllAppointmentsByUser(@RequestParam(value = "size", defaultValue = "7") int size,
                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                            HttpServletRequest request, Model model) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            ZhemUserDto user = zhemUserService.findUserByPhone(principal.getName());
            model.addAttribute("appointments",
                    this.appointmentService.findAllAppointmentsByUser(user.getId(), PageRequest.of(page, size)));
            return "user/appointments/appointments";
        } else {
            return "redirect:/user/logout";
        }
    }

}
