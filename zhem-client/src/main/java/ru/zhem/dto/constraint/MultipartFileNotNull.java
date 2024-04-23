package ru.zhem.dto.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipartFileNotNullValidator.class)
@Documented
public @interface MultipartFileNotNull {

    String message() default "Empty MultipartFile";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
