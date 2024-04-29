package ru.zhem.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.zhem.common.constraint.CheckPhoneNumber;

@Data
public class ZhemUserUpdate {

    private Long id;

    @CheckPhoneNumber(message = "Поле должно иметь международный формат номера телефона")
    private String phone;

    @Email(message = "Поле должно иметь формат электронной почты")
    @Size(max = 256, message = "Поле должно быть не больше 256 символов")
    private String email;

    @NotBlank(message = "Поле не должно быть пустым и должно содержать хотя бы 2 символа")
    @Size(min = 2, max = 32, message = "Поле должно быть от 2 до 32 символов")
    private String firstName;

    @Size(max = 32, message = "Поле должно быть до 32 символов")
    private String lastName;

}

