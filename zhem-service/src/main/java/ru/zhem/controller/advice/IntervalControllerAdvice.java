package ru.zhem.controller.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.exception.IntervalIsBookedException;
import ru.zhem.exception.IntervalNotFoundException;
import ru.zhem.exception.IntervalWithDuplicateDateTimeException;

import java.time.DateTimeException;
import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class IntervalControllerAdvice {

    @ExceptionHandler(IntervalNotFoundException.class)
    public ResponseEntity<?> handlerIntervalNotFoundException(IntervalNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(IntervalIsBookedException.class)
    public ResponseEntity<?> handlerIntervalIsBookedException(IntervalIsBookedException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage()
        );
        problemDetail.setProperty("errors", Map.of("interval", exception.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

    @ExceptionHandler(IntervalWithDuplicateDateTimeException.class)
    public ResponseEntity<?> handlerIntervalWithDuplicateDateTimeException(IntervalWithDuplicateDateTimeException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage()
        );
        problemDetail.setProperty("errors", Map.of("time", exception.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

}
