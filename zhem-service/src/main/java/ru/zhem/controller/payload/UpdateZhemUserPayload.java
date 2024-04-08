package ru.zhem.controller.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.entity.constraints.CheckPhoneNumberOrNull;
import ru.zhem.entity.constraints.NullOrNotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateZhemUserPayload {

    @CheckPhoneNumberOrNull(message = "Поле должно иметь международный формат номера телефона или быть пустым")
    private String phone;

    @Size(max = 256, message = "Поле должно быть не больше 256 символов")
    @NullOrNotBlank(message = "Поле должно содержать хотя бы 2 символа, либо быть полностью пустым")
    private String password;

    @Email(message = "Поле должно иметь формат электронной почты")
    @Size(max = 256, message = "Поле должно быть не больше 256 символов")
    private String email;

    @Size(min = 2, max = 32, message = "Поле должно быть от 2 до 32 символов")
    @NullOrNotBlank(message = "Поле должно содержать хотя бы 2 символа, либо быть полностью пустым")
    private String firstName;

    @Size(min = 2, max = 32, message = "Поле должно быть от 2 до 32 символов")
    @NullOrNotBlank(message = "Поле должно содержать хотя бы 2 символа, либо быть полностью пустым")
    private String lastName;

}
