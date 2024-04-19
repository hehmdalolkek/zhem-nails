package ru.zhem.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.zhem.dto.constraints.CheckPhoneNumber;
import ru.zhem.entity.constraints.Password;
import ru.zhem.entity.constraints.PasswordMatcher;

import java.util.Set;

@PasswordMatcher(message = "Пароли не совпадают")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class ZhemUser extends BaseEntity {

    @CheckPhoneNumber(message = "Поле должно иметь международный формат номера телефона")
    private String phone;

    @Password
    private String password;

    private String confirmPassword;

    @Email(message = "Поле должно иметь формат электронной почты")
    @Size(max = 256, message = "Поле должно быть не больше 256 символов")
    private String email;

    @NotBlank(message = "Поле не должно быть пустым и должно содержать хотя бы 2 символа")
    @Size(min = 2, max = 32, message = "Поле должно быть от 2 до 32 символов")
    private String firstName;

    @Size(max = 32, message = "Поле должно быть до 32 символов")
    private String lastName;

    private Set<Role> roles;

    @Override
    public String toString() {
        return "ZhemUser{" +
                "phone='" + phone + '\'' +
                ", password='[PROTECTED]" + '\'' +
                ", confirmPassword='[PROTECTED]" + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roles=" + roles +
                '}';
    }
}

