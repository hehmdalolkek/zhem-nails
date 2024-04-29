package ru.zhem.common.exceptions;

import lombok.Getter;
import org.springframework.http.ProblemDetail;

@Getter
public class NotFoundException extends RuntimeException {

    private final ProblemDetail problemDetail;

    public NotFoundException(ProblemDetail problemDetail) {
        this.problemDetail = problemDetail;
    }

}
