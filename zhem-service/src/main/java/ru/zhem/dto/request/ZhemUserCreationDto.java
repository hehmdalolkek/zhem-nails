package ru.zhem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.zhem.common.constraint.CheckPhoneNumber;
import ru.zhem.common.constraint.NullOrNotBlank;
import ru.zhem.dto.response.RoleDto;

import java.util.Set;

@Data
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

    @Size(min = 2, max = 32, message = "Фамилия должна содержать от 2 до 32 символов")
    @NullOrNotBlank(message = "Фамилия должна содержать хотя бы 2 символа, либо быть полностью пустым")
    private String lastName;

    @NotNull(message = "Роли должны быть указаны")
    private Set<RoleDto> roles;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "[PROTECTED]";
    }

}
