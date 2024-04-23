package ru.zhem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.zhem.dto.request.constraint.CheckPhoneNumber;
import ru.zhem.dto.request.constraint.NullOrNotBlank;
import ru.zhem.dto.response.RoleDto;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZhemUserCreationDto {

    @CheckPhoneNumber(message = "Поле должно иметь международный формат номера телефона")
    private String phone;

    @NotBlank(message = "Поле не должно быть пустым и должно содержать хотя бы один символ")
    @Size(max = 256, message = "Поле должно быть не больше 256 символов")
    private String password;

    @Email(message = "Поле должно иметь формат электронной почты")
    @Size(max = 256, message = "Поле должно быть не больше 256 символов")
    private String email;

    @NotBlank(message = "Поле не должно быть пустым и должно содержать хотя бы 2 символа")
    @Size(min = 2, max = 32, message = "Поле должно быть от 2 до 32 символов")
    private String firstName;

    @Size(min = 2, max = 32, message = "Поле должно быть от 2 до 32 символов")
    @NullOrNotBlank(message = "Поле должно содержать хотя бы 2 символа, либо быть полностью пустым")
    private String lastName;

    @NotNull(message = "Поле не должно быть пустым")
    private Set<RoleDto> roles;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "[PROTECTED]";
    }

}
