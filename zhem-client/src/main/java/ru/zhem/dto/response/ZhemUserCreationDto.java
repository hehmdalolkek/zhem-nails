package ru.zhem.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.zhem.common.constraint.CheckPhoneNumber;
import ru.zhem.dto.request.RoleDto;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZhemUserCreationDto {

    @CheckPhoneNumber(message = "Необходимо корректно ввести российский номер телефона в международном формате")
    private String phone;

    @NotBlank(message = "Пароль должен быть заполнен")
    @Size(max = 256, message = "Пароль должен содержать не больше 256 символов")
    private String password;

    @Email(message = "Электронная почта должна иметь формат электронной почты")
    @Size(max = 256, message = "Электронная почта должна содержать не больше 256 символов")
    private String email;

    @NotBlank(message = "Имя должно содержать хотя бы 2 символа, либо быть полностью пустым")
    @Size(min = 2, max = 32, message = "Имя должно содержать от 2 до 32 символов")
    private String firstName;

    @Size(max = 32, message = "Фамилия должна содержать до 32 символов")
    private String lastName;

    @NotNull(message = "Роли должны быть указаны")
    private Set<RoleDto> roles;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "[PROTECTED]";
    }

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

}
