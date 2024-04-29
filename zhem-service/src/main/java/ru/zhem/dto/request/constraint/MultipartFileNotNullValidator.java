package ru.zhem.dto.request.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class MultipartFileNotNullValidator implements ConstraintValidator<MultipartFileNotNull, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && !value.isEmpty();
    }
}
