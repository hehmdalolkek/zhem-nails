package ru.zhem.exception;

public class ZhemUserNotFoundException extends RuntimeException {

    public ZhemUserNotFoundException(String message) {
        super(message);
    }

}
