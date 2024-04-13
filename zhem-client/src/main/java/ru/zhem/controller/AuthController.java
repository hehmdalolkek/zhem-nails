package ru.zhem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.zhem.entity.ZhemUser;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.service.ZhemUserService;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private final ZhemUserService zhemUserService;

    @GetMapping("/admin/login")
    public String initLoginAdminPage() {
        return "/admin/auth/login";
    }

    @GetMapping("/admin/logout")
    public String logoutAdmin(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/admin/login?logout";
    }

    @GetMapping("/user/login")
    public String initLoginUserPage() {
        return "/user/auth/login";
    }

    @GetMapping("/user/logout")
    public String logoutUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/user/login?logout";
    }

    @GetMapping("/user/registration")
    public String initRegistrationUserPage() {
        return "/user/auth/registration";
    }

    @PostMapping("/user/registration")
    public String registrationUser(@Valid ZhemUser user, BindingResult bindingResult,
                                   HttpServletResponse response, Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("enteredData", user);
            model.addAttribute("errors", errors);
            return "/user/auth/registration";
        } else {
            try {
                this.zhemUserService.createUser(user, false);
                return "redirect:/user/login";
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                model.addAttribute("enteredData", user);
                model.addAttribute("errors", exception.getErrors());
                return "/user/auth/registration";
            }
        }
    }

}
