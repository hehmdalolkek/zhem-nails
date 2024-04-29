package ru.zhem.common.exceptions;

public class ZhemUserNotFoundException extends RuntimeException {
    public ZhemUserNotFoundException(String message) {
        super(message);
    }
}
