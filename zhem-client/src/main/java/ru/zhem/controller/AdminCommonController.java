package ru.zhem.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.entity.ZhemUser;
import ru.zhem.entity.ZhemUserUpdate;
import ru.zhem.entity.ZhemUserUpdatePassword;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.exceptions.NotFoundException;
import ru.zhem.service.ZhemUserService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminCommonController {

    private final ZhemUserService zhemUserService;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminCommonController(ZhemUserService zhemUserService, PasswordEncoder passwordEncoder,
                                 @Qualifier("zhemUserDetailsService") UserDetailsService userDetailsService) {
        this.zhemUserService = zhemUserService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String initProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        try {
            ZhemUserDto user = zhemUserService.findUserByPhone(principal.getUsername());
            model.addAttribute("user", user);
            return "/admin/common/profile";
        } catch (UsernameNotFoundException exception) {
            throw new NotFoundException(ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND, exception.getMessage()
            ));
        }
    }

    @PostMapping("/profile/update")
    public String updateUser(@Valid @ModelAttribute("user") ZhemUserUpdate user, BindingResult bindingResult,
                             HttpServletResponse response, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Данные не изменены");
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("errors", errors);
        } else {
            try {
                ZhemUser zhemUser = ZhemUser.builder()
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build();
                zhemUserService.updateUser(user.getId(), zhemUser);
                redirectAttributes.addFlashAttribute("message", "Данные изменены успешно");
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("errors", exception.getErrors());
                return "redirect:/admin/profile";
            }
        }

        return "redirect:/admin/profile";
    }

    @PostMapping("/profile/update-password")
    public String updateUserPassword(@Valid @ModelAttribute("user") ZhemUserUpdatePassword user,
                                     BindingResult bindingResult, HttpServletResponse response,
                                     RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("messagePassword", "Пароль не изменен");
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("errors", errors);
        } else {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getPhone());
            if (this.passwordEncoder.matches(user.getCurrentPassword(), userDetails.getPassword())) {
                this.zhemUserService.updateUser(user.getId(), ZhemUser.builder().password(user.getPassword()).build());
                redirectAttributes.addFlashAttribute("messagePassword", "Пароль изменен успешно");
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("errors", Map.of("currentPassword", "Неверный пароль"));
            }
        }

        return "redirect:/admin/profile";
    }

}
