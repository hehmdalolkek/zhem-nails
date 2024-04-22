package ru.zhem.exception;

public class FileWithDuplicateNameException extends RuntimeException {
    public FileWithDuplicateNameException(String message) {
        super(message);
    }
}
