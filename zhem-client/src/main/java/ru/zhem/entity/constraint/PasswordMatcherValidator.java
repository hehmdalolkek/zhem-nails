package ru.zhem.entity.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.zhem.entity.ZhemUser;
import ru.zhem.entity.ZhemUserUpdatePassword;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatcher, Object> {

    private String message;

    @Override
    public void initialize(PasswordMatcher passwordMatcher) {
        this.message = passwordMatcher.message();
    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean isValid;

        if (value instanceof ZhemUser user) {
            isValid = user.getPassword().equals(user.getConfirmPassword());
        } else {
            ZhemUserUpdatePassword user = (ZhemUserUpdatePassword) value;
            isValid = user.getPassword().equals(user.getConfirmPassword());
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("passwordMatchers").addConstraintViolation();
        }

        return isValid;
    }
}
