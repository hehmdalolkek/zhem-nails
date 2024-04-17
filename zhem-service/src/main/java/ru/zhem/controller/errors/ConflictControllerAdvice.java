package ru.zhem.controller.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.exception.BadRequestException;
import ru.zhem.exception.ConflictException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ConflictControllerAdvice {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handlerConflictException(ConflictException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, "Conflict"
        );

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        problemDetail.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

}
