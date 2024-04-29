package ru.zhem.common.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrNotBlankValidator.class)
@Documented
public @interface NullOrNotBlank {

    String message() default "String should be null or not be blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}