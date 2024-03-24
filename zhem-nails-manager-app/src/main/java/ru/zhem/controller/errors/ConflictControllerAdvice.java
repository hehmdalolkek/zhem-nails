package ru.zhem.controller.errors;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ConflictControllerAdvice {

    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public String handleHttpClientErrorException(HttpClientErrorException exception, HttpServletResponse response) {
        response.setStatus(HttpStatus.CONFLICT.value());
        return "service/common/errors/409";
    }
}
