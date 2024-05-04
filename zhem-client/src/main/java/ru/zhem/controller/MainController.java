package ru.zhem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.zhem.service.interfaces.PostService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class MainController {

    private final PostService postService;

    @GetMapping
    public String getIndexPage(Model model) {
        model.addAttribute("posts", this.postService.findAllPosts(4, 0));
        return "common/index";
    }

}
