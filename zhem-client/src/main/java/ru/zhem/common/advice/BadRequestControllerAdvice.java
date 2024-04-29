package ru.zhem.common.advice;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.common.exceptions.BadRequestException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
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
