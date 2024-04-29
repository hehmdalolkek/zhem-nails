package ru.zhem.common.advice;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.zhem.common.exceptions.NotFoundException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NotFoundControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public String handlerNotFoundException(NotFoundException exception, Model model,
                                             HttpServletResponse response) {
        ProblemDetail problemDetail = exception.getProblemDetail();
        model.addAttribute("status", problemDetail.getStatus());
        model.addAttribute("title", problemDetail.getTitle());
        model.addAttribute("detail", problemDetail.getDetail());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return "common/errors/error";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handlerNoResourceFoundException(NoResourceFoundException exception, Model model,
                                             HttpServletResponse response) {
        ProblemDetail problemDetail = exception.getBody();
        model.addAttribute("status", problemDetail.getStatus());
        model.addAttribute("title", problemDetail.getTitle());
        model.addAttribute("detail", problemDetail.getDetail());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return "common/errors/error";
    }


}
