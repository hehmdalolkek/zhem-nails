package ru.zhem.client;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class BadRequestException extends RuntimeException {

    private final Map<String, String> errors;

    public BadRequestException(Map<String, String> errors) {
        this.errors = errors;
    }

}
