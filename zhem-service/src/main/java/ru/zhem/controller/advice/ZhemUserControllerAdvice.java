package ru.zhem.controller.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.exception.*;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ZhemUserControllerAdvice {

    @ExceptionHandler(ZhemUserNotFoundException.class)
    public ResponseEntity<?> handlerZhemUserNotFoundException(ZhemUserNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(ZhemUserWithDuplicateEmailException.class)
    public ResponseEntity<?> handlerZhemUserWithDuplicateEmailException(ZhemUserWithDuplicateEmailException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

    @ExceptionHandler(ZhemUserWithDuplicatePhoneException.class)
    public ResponseEntity<?> handlerZhemUserWithDuplicatePhoneException(ZhemUserWithDuplicatePhoneException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

}
