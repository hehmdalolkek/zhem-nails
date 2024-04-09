package ru.zhem.exception;

public class IntervalWithDuplicateDateTimeException extends RuntimeException {

    public IntervalWithDuplicateDateTimeException(String message) {
        super(message);
    }
}
