package ru.zhem.common.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.common.exception.RoleNotFoundException;
import ru.zhem.common.exception.RoleWithDuplicateTitleException;

import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RoleControllerAdvice {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handlerRoleNotFoundException(RoleNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(RoleWithDuplicateTitleException.class)
    public ResponseEntity<?> handlerRoleWithDuplicateTitleException(RoleWithDuplicateTitleException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage()
        );
        problemDetail.setProperty("errors", Map.of("title", "Роль с указанным именем уже существует"));
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

}
