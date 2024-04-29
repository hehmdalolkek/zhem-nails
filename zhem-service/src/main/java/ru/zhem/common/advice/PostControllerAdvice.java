package ru.zhem.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zhem.common.exception.EmptyFileException;
import ru.zhem.common.exception.FileInvalidTypeException;
import ru.zhem.common.exception.PostNotFoundException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class PostControllerAdvice {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<?> handlerPostNotFoundException(PostNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<?> handlerEmptyFileException(EmptyFileException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, exception.getMessage()
        );
        log.warn("Exception caught [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(FileInvalidTypeException.class)
    public ResponseEntity<?> handlerFileInvalidTypeException(FileInvalidTypeException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, exception.getMessage()
        );
        log.warn("Exception caught [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

}
