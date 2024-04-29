package ru.zhem.common.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomBindException extends RuntimeException {

    private final Map<String, String> errors;

    public CustomBindException(Map<String, String> errors) {
        this.errors = errors;
    }
}
