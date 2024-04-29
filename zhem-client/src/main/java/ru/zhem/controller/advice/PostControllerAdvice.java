package ru.zhem.controller.advice;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.exceptions.IntervalNotFoundException;
import ru.zhem.exceptions.PostNotFoundException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PostControllerAdvice {

    @ExceptionHandler(PostNotFoundException.class)
    public String handlerPostNotFoundException(PostNotFoundException exception, Model model,
                                                   HttpServletResponse response) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("title", HttpStatus.NOT_FOUND.getReasonPhrase());
        model.addAttribute("detail", exception.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return "common/errors/error";
    }
}
