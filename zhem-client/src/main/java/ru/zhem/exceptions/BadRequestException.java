package ru.zhem.exceptions;

import lombok.Getter;
import org.springframework.http.ProblemDetail;

@Getter
public class BadRequestException extends RuntimeException {

    private final ProblemDetail problemDetail;

    public BadRequestException(ProblemDetail problemDetail) {
        this.problemDetail = problemDetail;
    }

}
