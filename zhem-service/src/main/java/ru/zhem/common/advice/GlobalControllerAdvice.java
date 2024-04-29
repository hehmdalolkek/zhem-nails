package ru.zhem.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Bad credentials"
        );
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : exception.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        problemDetail.setProperty("errors", errors);
        return ResponseEntity.badRequest()
                .body(problemDetail);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<?> handlerDateTimeException(DateTimeException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, exception.getMessage()
        );
        return ResponseEntity.badRequest()
                .body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerOtherExceptions(Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()
        );
        return ResponseEntity.internalServerError()
                .body(problemDetail);
    }

}
