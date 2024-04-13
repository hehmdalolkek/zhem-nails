package ru.zhem.entity.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;
import java.util.Properties;

public class PasswordConstraintsValidator implements ConstraintValidator<Password, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        PasswordValidator passwordValidator = new PasswordValidator(
                new PropertiesMessageResolver(getProperties()),
                Arrays.asList(
                        new LengthRule(8, 32),
                        new CharacterRule(EnglishCharacterData.UpperCase, 1),
                        new CharacterRule(EnglishCharacterData.LowerCase, 1),
                        new CharacterRule(EnglishCharacterData.Digit, 1),
                        new WhitespaceRule()
                )
        );

        RuleResult result = passwordValidator.validate(new PasswordData(value));

        if (result.isValid()) {
            return true;
        }

        context.buildConstraintViolationWithTemplate(passwordValidator.getMessages(result)
                        .stream()
                        .findFirst()
                        .get())
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;

    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("TOO_LONG", "Пароль должен быть не более 32 символов");
        props.setProperty("TOO_SHORT", "Пароль должен быть не менее 8 символов");
        props.setProperty("INSUFFICIENT_UPPERCASE", "Должна быть хотя бы одна прописная буква");
        props.setProperty("INSUFFICIENT_LOWERCASE", "Должна быть хотя бы одна строчная буква");
        props.setProperty("INSUFFICIENT_DIGIT", "Должно быть хотя бы одно число");
        props.setProperty("ILLEGAL_WHITESPACE", "Не должно быть пробелов");
        return props;
    }
}
