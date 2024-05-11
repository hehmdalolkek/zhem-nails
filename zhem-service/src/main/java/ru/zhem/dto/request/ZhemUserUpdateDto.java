package ru.zhem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.zhem.common.constraint.CheckPhoneNumberOrNull;
import ru.zhem.common.constraint.NullOrNotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZhemUserUpdateDto {

    @CheckPhoneNumberOrNull(message = "Необходимо корректно ввести российский номер телефона в международном" +
            " формате или оставить поле пустым")
    private String phone;

    @Size(max = 256, message = "Пароль должен содержать не больше 256 символов")
    @NullOrNotBlank(message = "Пароль должен содержать хотя бы 2 символа, либо быть полностью пустым")
    private String password;

    @Email(message = "Электронная почта должна иметь формат электронной почты")
    @Size(max = 256, message = "Электронная почта должна содержать не больше 256 символов")
    private String email;

    @Size(min = 2, max = 32, message = "Имя должно содержать до 32 символов")
    @NullOrNotBlank(message = "Имя должно содержать хотя бы 2 символа, либо быть полностью пустым")
    private String firstName;

    @Size(min = 2, max = 32, message = "Фамилия должна содержать от 2 до 32 символов")
    @NullOrNotBlank(message = "Фамилия должна содержать хотя бы 2 символа, либо быть полностью пустым")
    private String lastName;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "[PROTECTED]";
    }

}
