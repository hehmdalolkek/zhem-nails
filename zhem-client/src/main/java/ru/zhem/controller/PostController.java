package ru.zhem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zhem.exceptions.NotFoundException;
import ru.zhem.exceptions.PostNotFoundException;
import ru.zhem.service.interfaces.PostService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/portfolio")
public class PostController {

    private final PostService postService;

    @GetMapping
    public String getPortfolio(@RequestParam(value = "size", defaultValue = "9") int size,
                               @RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        model.addAttribute("posts", this.postService.findAllPosts(size, page));
        return "/common/portfolio/posts";
    }

    @GetMapping("/{postId:\\d+}")
    public String getPostById(@PathVariable("postId") long postId, Model model) {
        try {
            model.addAttribute("post", this.postService.findPostById(postId));
            return "/common/portfolio/post";
        } catch (PostNotFoundException exception) {
            throw new NotFoundException(ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND, exception.getMessage()));
        }
    }

}
