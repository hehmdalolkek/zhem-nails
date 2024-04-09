package ru.zhem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreationDto {

    @NotBlank(message = "Поле должно быть заполнено")
    @Size(min = 2, max = 32, message = "Поле должно быть от 2 до 32 символов")
    private String title;

}
