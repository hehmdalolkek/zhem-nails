package ru.zhem.common.exception;

public class ZhemServiceWithDuplicateTitleException extends RuntimeException {
    public ZhemServiceWithDuplicateTitleException(String message) {
        super(message);
    }
}
