package ru.zhem.controller.advice;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.exceptions.BadRequestException;

import java.time.DateTimeException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DateTimeException.class)
    public String handlerDateTimeException(BadRequestException exception, Model model,
                                           HttpServletResponse response) {
        ProblemDetail problemDetail = exception.getProblemDetail();
        model.addAttribute("status", problemDetail.getStatus());
        model.addAttribute("title", problemDetail.getTitle());
        model.addAttribute("detail", problemDetail.getDetail());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return "common/errors/error";
    }

    @ExceptionHandler(Exception.class)
    public String handlerOtherExceptions(Exception exception, Model model,
                                         HttpServletResponse response) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("title", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute("detail", exception.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return "common/errors/error";
    }

}
