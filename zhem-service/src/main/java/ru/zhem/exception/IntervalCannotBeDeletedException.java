package ru.zhem.exception;

public class IntervalCannotBeDeletedException extends RuntimeException {
    public IntervalCannotBeDeletedException(String message) {
        super(message);
    }
}
