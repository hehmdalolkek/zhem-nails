package ru.zhem.entity.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;
import java.util.regex.Pattern;

public class CheckPhoneNumberValidator implements ConstraintValidator<CheckPhoneNumber, String> {

    @Override
    public void initialize(CheckPhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !Objects.isNull(value) &&
                Pattern.matches("^79[0-9]{9}$", value);
    }
}
