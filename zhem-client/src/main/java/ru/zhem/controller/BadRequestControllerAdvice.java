package ru.zhem.controller;

import org.springframework.http.ProblemDetail;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.exceptions.BadRequestException;

@ControllerAdvice
public class BadRequestControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public String handlerBadRequestException(BadRequestException exception, Model model) {
        ProblemDetail problemDetail = exception.getProblemDetail();
        model.addAttribute("status", problemDetail.getStatus());
        model.addAttribute("title", problemDetail.getTitle());
        model.addAttribute("detail", problemDetail.getDetail());
        return "common/errors/error";
    }

}
