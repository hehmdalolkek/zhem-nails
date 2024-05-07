package ru.zhem.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.controller.util.ControllerUtil;
import ru.zhem.dto.request.ZhemServiceDto;
import ru.zhem.dto.response.ZhemServiceCreationDto;
import ru.zhem.common.exceptions.CustomBindException;
import ru.zhem.service.interfaces.ZhemServiceService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/services")
public class AdminZhemServiceController {

    private final ZhemServiceService zhemServiceService;

    private final ControllerUtil controllerUtil;

    @GetMapping
    public String showAllServices(Model model) {
        List<ZhemServiceDto> services = this.zhemServiceService.findAllServices();
        model.addAttribute("services", services);
        return "admin/services/services";
    }

    @PostMapping("/delete")
    public String deleteService(Integer serviceId, RedirectAttributes redirectAttributes) {
        this.zhemServiceService.deleteServiceById(serviceId);
        redirectAttributes.addFlashAttribute("message", "Услуга удалена");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/admin/services";
    }

    @PostMapping("/create")
    public String createService(@Valid ZhemServiceCreationDto service, BindingResult bindingResult,
                                HttpServletResponse response, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Ошибка добавления");
        redirectAttributes.addFlashAttribute("messageType", "danger");
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = this.controllerUtil.getErrors(bindingResult);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("enteredData", service);
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/admin/services";
        } else {
            try {
                this.zhemServiceService.createService(service);
                redirectAttributes.addFlashAttribute("message", "Услуга добавлена");
                redirectAttributes.addFlashAttribute("messageType", "success");

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
