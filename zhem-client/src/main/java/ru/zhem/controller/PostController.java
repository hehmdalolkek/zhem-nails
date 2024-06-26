package ru.zhem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zhem.service.interfaces.PostService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/portfolio")
public class PostController {

    private final PostService postService;

    @GetMapping
    public String getPortfolio(@RequestParam(value = "size", defaultValue = "12") int size,
                               @RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        model.addAttribute("posts", this.postService.findAllPosts(size, page));
        return "common/portfolio/posts";
    }

}
