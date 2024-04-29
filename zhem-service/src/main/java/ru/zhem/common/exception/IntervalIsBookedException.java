package ru.zhem.common.exception;

public class IntervalIsBookedException extends RuntimeException {
    public IntervalIsBookedException(String message) {
        super(message);
    }
}
