package ru.zhem.entity.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class CheckPhoneNumberValidator implements ConstraintValidator<CheckPhoneNumber, BigDecimal> {

    @Override
    public void initialize(CheckPhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return (value.compareTo(BigDecimal.valueOf(69999999999L)) > 0)
                && (value.compareTo(BigDecimal.valueOf(80000000000L)) < 0);
    }
}
