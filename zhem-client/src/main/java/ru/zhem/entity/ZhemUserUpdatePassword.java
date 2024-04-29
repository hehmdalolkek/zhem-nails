package ru.zhem.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.zhem.common.constraint.CheckPhoneNumber;
import ru.zhem.common.constraint.Password;
import ru.zhem.common.constraint.PasswordMatcher;

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

    @Override
    public String toString() {
        return "ZhemUserUpdatePassword{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", currentPassword='[PROTECTED]" + '\'' +
                ", password='[PROTECTED]" + '\'' +
                ", confirmPassword='[PROTECTED]" + '\'' +
                '}';
    }
}
