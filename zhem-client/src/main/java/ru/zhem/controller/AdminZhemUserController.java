package ru.zhem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.service.interfaces.ZhemUserService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/users")
public class AdminZhemUserController {

    private final ZhemUserService zhemUserService;

    @GetMapping
    public String showAllClientsByPage(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "7") int size) {
        Page<ZhemUserDto> users = this.zhemUserService.findAllClientsByPage(page, size);
        model.addAttribute("users", users);
        return "admin/users/users";
    }

}
