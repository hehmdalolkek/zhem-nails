package ru.zhem.entity.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.zhem.entity.ZhemUser;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatcher, Object> {

    private String message;

    @Override
    public void initialize(PasswordMatcher passwordMatcher) {
        this.message = passwordMatcher.message();
    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        ZhemUser user = (ZhemUser) value;
        boolean isValid = user.getPassword().equals(user.getConfirmPassword());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("passwordMatchers").addConstraintViolation();
        }

        return isValid;
    }
}
