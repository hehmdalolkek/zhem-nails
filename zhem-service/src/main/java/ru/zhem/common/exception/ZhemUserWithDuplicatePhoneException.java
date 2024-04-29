package ru.zhem.common.exception;

public class ZhemUserWithDuplicatePhoneException extends RuntimeException {

    public ZhemUserWithDuplicatePhoneException(String message) {
        super(message);
    }
}
