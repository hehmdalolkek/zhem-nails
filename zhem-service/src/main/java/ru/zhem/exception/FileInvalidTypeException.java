package ru.zhem.exception;

public class FileInvalidTypeException extends RuntimeException {
    public FileInvalidTypeException(String message) {
        super(message);
    }
}
