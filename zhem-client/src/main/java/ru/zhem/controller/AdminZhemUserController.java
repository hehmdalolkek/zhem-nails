package ru.zhem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zhem.client.ZhemUserRestClient;
import ru.zhem.client.response.PaginatedResponse;
import ru.zhem.dto.request.ZhemUserDto;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/users")
public class AdminZhemUserController {

    private final ZhemUserRestClient zhemUserRestClient;

    @GetMapping
    public String showAllClientsByPage(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "15") int size) {
        PaginatedResponse<ZhemUserDto> users = this.zhemUserRestClient.findAllUsersByPage(page, size);
        model.addAttribute("users", users);
        return "/admin/users/users";
    }

}
