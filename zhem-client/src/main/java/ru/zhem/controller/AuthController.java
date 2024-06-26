package ru.zhem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.zhem.controller.util.ControllerUtil;
import ru.zhem.entity.ZhemUser;
import ru.zhem.common.exceptions.CustomBindException;
import ru.zhem.service.interfaces.ZhemUserService;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@Slf4j
public class AuthController {

    private final ZhemUserService zhemUserService;

    private final ControllerUtil controllerUtil;

    @GetMapping("/admin/login")
    public String initLoginAdminPage() {
        if (!zhemUserService.adminIsExists()) {
            return "redirect:/admin/registration";
        }
        return "admin/auth/login";
    }

    @GetMapping("/admin/registration")
    public String initRegistrationAdminPage() {
        if (this.zhemUserService.adminIsExists()) {
            return "redirect:/admin/login";
        }
        return "admin/auth/registration";
    }

    @PostMapping("/admin/registration")
    public String registrationAdmin(@Valid ZhemUser user, BindingResult bindingResult,
                                    HttpServletRequest request, HttpServletResponse response, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = this.controllerUtil.getErrors(bindingResult);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("enteredData", user);
            model.addAttribute("errors", errors);
            return "admin/auth/registration";
        } else {
            try {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                    new SecurityContextLogoutHandler().logout(request, response, auth);
                }
                this.zhemUserService.createUser(user, true);
                log.info("Admin registered successfully");
                return "redirect:/admin/login";
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                model.addAttribute("enteredData", user);
                model.addAttribute("errors", exception.getErrors());
                return "admin/auth/registration";
            }
        }
    }


    @GetMapping("/user/login")
    public String initLoginUserPage() {
        return "user/auth/login";
    }

    @GetMapping("/user/registration")
    public String initRegistrationUserPage() {
        return "user/auth/registration";
    }

    @PostMapping("/user/registration")
    public String registrationUser(@Valid ZhemUser user, BindingResult bindingResult,
                                   HttpServletResponse response, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = this.controllerUtil.getErrors(bindingResult);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("enteredData", user);
            model.addAttribute("errors", errors);
            return "user/auth/registration";
        } else {
            try {
                this.zhemUserService.createUser(user, false);
                return "redirect:/user/login";
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                model.addAttribute("enteredData", user);
                model.addAttribute("errors", exception.getErrors());
                return "user/auth/registration";
            }
        }
    }

}
