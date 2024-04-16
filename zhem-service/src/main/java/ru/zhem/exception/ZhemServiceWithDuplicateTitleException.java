package ru.zhem.exception;

public class ZhemServiceWithDuplicateTitleException extends RuntimeException {
    public ZhemServiceWithDuplicateTitleException(String message) {
        super(message);
    }
}
