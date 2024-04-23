package ru.zhem.controller.advice;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.exceptions.BadRequestException;

@ControllerAdvice
public class BadRequestControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public String handlerBadRequestException(BadRequestException exception, Model model,
                                             HttpServletResponse response) {
        ProblemDetail problemDetail = exception.getProblemDetail();
        model.addAttribute("status", problemDetail.getStatus());
        model.addAttribute("title", problemDetail.getTitle());
        model.addAttribute("detail", problemDetail.getDetail());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return "common/errors/error";
    }

}
