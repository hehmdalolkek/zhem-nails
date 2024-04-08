package ru.zhem.exception;

public class ZhemUserWithDuplicatePhoneException extends RuntimeException {

    public ZhemUserWithDuplicatePhoneException(String message) {
        super(message);
    }
}
