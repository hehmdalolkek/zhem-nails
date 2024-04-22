package ru.zhem.exception;

public class ExampleNotFoundException extends RuntimeException {
    public ExampleNotFoundException(String message) {
        super(message);
    }
}