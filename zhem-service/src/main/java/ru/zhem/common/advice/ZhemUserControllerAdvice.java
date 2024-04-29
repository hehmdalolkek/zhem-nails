package ru.zhem.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.common.exception.ZhemUserNotFoundException;
import ru.zhem.common.exception.ZhemUserWithDuplicateEmailException;
import ru.zhem.common.exception.ZhemUserWithDuplicatePhoneException;

import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ZhemUserControllerAdvice {

    @ExceptionHandler(ZhemUserNotFoundException.class)
    public ResponseEntity<?> handlerZhemUserNotFoundException(ZhemUserNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage()
        );
        log.warn("Exception caught [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(ZhemUserWithDuplicateEmailException.class)
    public ResponseEntity<?> handlerZhemUserWithDuplicateEmailException(ZhemUserWithDuplicateEmailException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage()
        );
        log.warn("Exception caught [{}: {}]", exception.getClass().getName(), exception.getMessage());
        problemDetail.setProperty("errors", Map.of("email", "Пользователь с указанной электронной почтой уже существует"));
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

    @ExceptionHandler(ZhemUserWithDuplicatePhoneException.class)
    public ResponseEntity<?> handlerZhemUserWithDuplicatePhoneException(ZhemUserWithDuplicatePhoneException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage()
        );
        log.warn("Exception caught [{}: {}]", exception.getClass().getName(), exception.getMessage());
        problemDetail.setProperty("errors", Map.of("phone", "Пользователь с указанным номером телефона уже существует"));
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

}
