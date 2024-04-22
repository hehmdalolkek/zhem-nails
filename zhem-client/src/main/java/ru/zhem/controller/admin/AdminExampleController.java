package ru.zhem.controller.admin;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.dto.response.ExampleCreationDto;
import ru.zhem.dto.response.ExampleUpdateDto;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.exceptions.FileInvalidType;
import ru.zhem.service.ExampleService;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/portfolio")
public class AdminExampleController {

    private final ExampleService exampleService;

    @GetMapping
    public String getPortfolio(@RequestParam(value = "size", defaultValue = "9") int size,
                               @RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        model.addAttribute("examples", this.exampleService.findAllExamples(size, page));
        return "admin/portfolio/examples";
    }

    @PostMapping("/create")
    public String createExample(@Valid ExampleCreationDto exampleDto, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes, HttpServletResponse response) {
        redirectAttributes.addFlashAttribute("message", "Ошибка добавления");
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("enteredData", exampleDto);
        } else {
            try {
                this.exampleService.createExample(exampleDto);
                redirectAttributes.addFlashAttribute("message", "Успешно добавлено");
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("errors", exception.getErrors());
                redirectAttributes.addFlashAttribute("enteredData", exampleDto);
                return "redirect:/admin/portfolio";
            } catch (FileInvalidType exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("errors", Map.of("image", exception.getMessage()));
                redirectAttributes.addFlashAttribute("enteredData", exampleDto);
                return "redirect:/admin/portfolio";
            }
        }
        return "redirect:/admin/portfolio";
    }

    @PostMapping("/delete")
    public String deleteExample(long exampleId) {
        this.exampleService.deleteById(exampleId);
        return "redirect:/admin/portfolio";
    }

}
