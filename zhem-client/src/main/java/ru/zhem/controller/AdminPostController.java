package ru.zhem.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.controller.util.ControllerUtil;
import ru.zhem.dto.response.PostCreationDto;
import ru.zhem.common.exceptions.CustomBindException;
import ru.zhem.service.interfaces.PostService;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/portfolio")
public class AdminPostController {

    private final PostService postService;

    private final ControllerUtil controllerUtil;

    @GetMapping
    public String getPortfolio(@RequestParam(value = "size", defaultValue = "9") int size,
                               @RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        model.addAttribute("posts", this.postService.findAllPosts(size, page));
        return "admin/portfolio/posts";
    }

    @GetMapping("/{postId:\\d+}")
    public String getPostById(@PathVariable("postId") long postId, Model model) {
        model.addAttribute("post", this.postService.findPostById(postId));
        return "admin/portfolio/post";
    }

    @PostMapping("/create")
    public String createPost(@Valid PostCreationDto postDto, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, HttpServletResponse response) {
        redirectAttributes.addFlashAttribute("message", "Ошибка добавления");
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = this.controllerUtil.getErrors(bindingResult);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("enteredData", postDto);
        } else {
            try {
                this.postService.createPost(postDto);
                redirectAttributes.addFlashAttribute("message", "Успешно добавлено");
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("errors", exception.getErrors());
                redirectAttributes.addFlashAttribute("enteredData", postDto);
                return "redirect:/admin/portfolio";
            }
        }
        return "redirect:/admin/portfolio";
    }

    @PostMapping("/delete")
    public String deletePost(long postId) {
        this.postService.deleteById(postId);
        return "redirect:/admin/portfolio";
    }

}
