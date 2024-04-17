package ru.zhem.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.zhem.dto.constraints.CheckPhoneNumber;
import ru.zhem.entity.constraints.Password;
import ru.zhem.entity.constraints.PasswordMatcher;

@PasswordMatcher(message = "Пароли не совпадают")
@Data
public class ZhemUserUpdatePassword {

    private Long id;

    @CheckPhoneNumber(message = "Поле должно иметь международный формат номера телефона")
    private String phone;

    @NotBlank(message = "Введите текущий пароль")
    private String currentPassword;

    @Password
    private String password;

    private String confirmPassword;

}
