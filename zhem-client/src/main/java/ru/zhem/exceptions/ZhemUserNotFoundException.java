package ru.zhem.exceptions;

public class ZhemUserNotFoundException extends RuntimeException {
    public ZhemUserNotFoundException(String message) {
        super(message);
    }
}
