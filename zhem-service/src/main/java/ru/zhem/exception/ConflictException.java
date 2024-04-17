package ru.zhem.exception;

import org.springframework.validation.BindingResult;

public class ConflictException extends Throwable {

    private final BindingResult bindingResult;

    public ConflictException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return this.bindingResult;
    }
}
