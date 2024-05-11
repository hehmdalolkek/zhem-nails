package ru.zhem.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.zhem.common.constraint.CheckPhoneNumberOrNull;
import ru.zhem.common.constraint.NullOrNotBlank;

@Data
@Builder
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

    @Size(max = 32, message = "Фамилия должна содержать до 32 символов")
    private String lastName;

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
