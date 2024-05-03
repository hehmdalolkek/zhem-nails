package ru.zhem.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.controller.util.ControllerUtil;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.entity.ZhemUser;
import ru.zhem.entity.ZhemUserUpdate;
import ru.zhem.entity.ZhemUserUpdatePassword;
import ru.zhem.common.exceptions.CustomBindException;
import ru.zhem.service.interfaces.ZhemUserService;

import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/user/profile")
public class UserProfileController {

    private final ZhemUserService zhemUserService;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final ControllerUtil controllerUtil;

    @Autowired
    public UserProfileController(ZhemUserService zhemUserService, PasswordEncoder passwordEncoder,
                                 @Qualifier("zhemUserDetailsService") UserDetailsService userDetailsService, ControllerUtil controllerUtil) {
        this.zhemUserService = zhemUserService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.controllerUtil = controllerUtil;
    }

    @GetMapping
    public String initProfilePage(Model model, Principal principal) {
        ZhemUserDto user = zhemUserService.findUserByPhone(principal.getName());
        model.addAttribute("user", user);
        return "user/common/profile";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") ZhemUserUpdate user, BindingResult bindingResult,
                             HttpServletResponse response, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Данные не изменены");
        redirectAttributes.addFlashAttribute("messageType", "danger");
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = this.controllerUtil.getErrors(bindingResult);
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
                redirectAttributes.addFlashAttribute("messageType", "success");
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("errors", exception.getErrors());
                return "redirect:/user/profile";
            }
        }

        return "redirect:/user/profile";
    }

    @PostMapping("/update-password")
    public String updateUserPassword(@Valid @ModelAttribute("user") ZhemUserUpdatePassword user,
                                     BindingResult bindingResult, HttpServletResponse response,
                                     RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("messagePassword", "Пароль не изменен");
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = this.controllerUtil.getErrors(bindingResult);
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

        return "redirect:/user/profile";
    }

}
