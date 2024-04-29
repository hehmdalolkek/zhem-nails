package ru.zhem.common.exception;

public class ZhemServiceNotFoundException extends RuntimeException {
    public ZhemServiceNotFoundException(String message) {
        super(message);
    }
}
