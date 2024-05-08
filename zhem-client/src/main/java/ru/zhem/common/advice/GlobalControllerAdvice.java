package ru.zhem.common.advice;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DateTimeException.class)
    public String handlerDateTimeException(DateTimeException exception, Model model,
                                           HttpServletResponse response) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("title", HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute("detail", "Invalid date");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return "common/errors/error";
    }

    @ExceptionHandler(Exception.class)
    public String handlerOtherExceptions(Model model,
                                         HttpServletResponse response) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("title", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return "common/errors/error";
    }

}
