package ru.zhem.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.common.exception.IntervalIsBookedException;
import ru.zhem.common.exception.IntervalNotFoundException;
import ru.zhem.common.exception.IntervalWithDuplicateDateTimeException;

import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class IntervalControllerAdvice {

    @ExceptionHandler(IntervalNotFoundException.class)
    public ResponseEntity<?> handlerIntervalNotFoundException(IntervalNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage()
        );
        log.warn("Exception caught [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(IntervalIsBookedException.class)
    public ResponseEntity<?> handlerIntervalIsBookedException(IntervalIsBookedException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage()
        );
        log.warn("Exception caught [{}: {}]", exception.getClass().getName(), exception.getMessage());
        problemDetail.setProperty("errors", Map.of("interval", "Интервал уже забронирован"));
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

    @ExceptionHandler(IntervalWithDuplicateDateTimeException.class)
    public ResponseEntity<?> handlerIntervalWithDuplicateDateTimeException(IntervalWithDuplicateDateTimeException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage()
        );
        log.warn("Exception caught [{}: {}]", exception.getClass().getName(), exception.getMessage());
        problemDetail.setProperty("errors", Map.of("time", "Интервал с заданным временем уже существует"));
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

}
