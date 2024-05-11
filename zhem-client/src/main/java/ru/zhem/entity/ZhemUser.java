package ru.zhem.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.zhem.common.constraint.CheckPhoneNumber;
import ru.zhem.common.constraint.Password;
import ru.zhem.common.constraint.PasswordMatcher;

import java.util.Set;

@PasswordMatcher(message = "Пароли не совпадают")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class ZhemUser extends BaseEntity {

    @CheckPhoneNumber(message = "Необходимо корректно ввести российский номер телефона в международном формате")
    private String phone;

    @Password
    private String password;

    private String confirmPassword;

    @Email(message = "Электронная почта должна иметь формат электронной почты")
    @Size(max = 256, message = "Электронная почта должна содержать не больше 256 символов")
    private String email;

    @NotBlank(message = "Имя должно содержать хотя бы 2 символа, либо быть полностью пустым")
    @Size(min = 2, max = 32, message = "Имя должно содержать от 2 до 32 символов")
    private String firstName;

    @Size(max = 32, message = "Фамилия должна содержать до 32 символов")
    private String lastName;

    private Set<Role> roles;

    @ToString.Include(name = "phone")
    public String getFormattedPhone() {
        return this.phone == null || this.phone.isBlank() ? null
                : "+" +
                this.phone.charAt(0) +
                "(" +
                this.phone.substring(1, 4) +
                ")" +
                this.phone.substring(4, 7) +
                "-" +
                this.phone.substring(7, 9) +
                "-" +
                this.phone.substring(9, 11);
    }

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

