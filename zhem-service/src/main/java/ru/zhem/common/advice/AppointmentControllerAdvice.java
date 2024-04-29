package ru.zhem.common.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.common.exception.AppointmentNotFoundException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppointmentControllerAdvice {

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<?> handlerAppointmentNotFoundException(AppointmentNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

}
