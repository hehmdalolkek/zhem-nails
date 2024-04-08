package ru.zhem.exception;

public class ZhemUserWithDuplicateEmailException extends RuntimeException {

    public ZhemUserWithDuplicateEmailException(String message) {
        super(message);
    }
}
