package ru.zhem.entity.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.util.Objects;

public class CheckPhoneNumberOrNullValidator implements ConstraintValidator<CheckPhoneNumberOrNull, BigDecimal> {

    @Override
    public void initialize(CheckPhoneNumberOrNull constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return Objects.isNull(value)
                || ((value.compareTo(BigDecimal.valueOf(69999999999L)) > 0)
                && (value.compareTo(BigDecimal.valueOf(80000000000L)) < 0));
    }
}
