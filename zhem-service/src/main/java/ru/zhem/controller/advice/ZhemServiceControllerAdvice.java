package ru.zhem.controller.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.exception.ZhemServiceNotFoundException;
import ru.zhem.exception.ZhemServiceWithDuplicateTitleException;

import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ZhemServiceControllerAdvice {

    @ExceptionHandler(ZhemServiceNotFoundException.class)
    public ResponseEntity<?> handlerZhemServiceNotFoundException(ZhemServiceNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(ZhemServiceWithDuplicateTitleException.class)
    public ResponseEntity<?> handlerZhemServiceWithDuplicateTitleException(ZhemServiceWithDuplicateTitleException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage()
        );
        problemDetail.setProperty("errors", Map.of("title", exception.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

}
