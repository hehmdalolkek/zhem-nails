package ru.zhem.controller.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zhem.service.ExampleService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/portfolio")
public class ExampleController {

    private final ExampleService exampleService;

    @GetMapping
    public String getPortfolio(@RequestParam(value = "size", defaultValue = "9") int size,
                               @RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        model.addAttribute("examples", this.exampleService.findAllExamples(size, page));
        return "common/portfolio/examples";
    }

}
