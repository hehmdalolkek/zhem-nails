package ru.zhem.exception;

public class RoleWithDuplicateTitleException extends RuntimeException {

    public RoleWithDuplicateTitleException(String message) {
        super(message);
    }

}
