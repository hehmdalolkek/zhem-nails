package ru.zhem.exception;

public class ZhemServiceNotFoundException extends RuntimeException {
    public ZhemServiceNotFoundException(String message) {
        super(message);
    }
}
