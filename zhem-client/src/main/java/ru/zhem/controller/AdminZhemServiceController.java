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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.dto.request.ZhemServiceDto;
import ru.zhem.dto.response.ZhemServiceCreationDto;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.service.ZhemServiceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/services")
public class AdminZhemServiceController {

    private final ZhemServiceService zhemServiceService;

    @GetMapping
    public String showAllServices(Model model) {
        List<ZhemServiceDto> services = this.zhemServiceService.findAllServices();
        model.addAttribute("services", services);
        return "/admin/services/services";
    }

    @PostMapping("/delete")
    public String deleteService(Integer serviceId) {
        this.zhemServiceService.deleteServiceById(serviceId);
        return "redirect:/admin/services";
    }

    @PostMapping("/create")
    public String createService(@Valid ZhemServiceCreationDto service, BindingResult bindingResult,
                                HttpServletResponse response, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("enteredData", service);
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/admin/services";
        } else {
            try {
                this.zhemServiceService.createService(service);
                return "redirect:/admin/services";
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("enteredData", service);
                redirectAttributes.addFlashAttribute("errors", exception.getErrors());
                return "redirect:/admin/services";
            }
        }
    }

}
