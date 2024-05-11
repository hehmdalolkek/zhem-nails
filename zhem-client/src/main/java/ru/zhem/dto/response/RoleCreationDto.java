package ru.zhem.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreationDto {

    @NotBlank(message = "Наименование должно быть заполнено")
    @Size(min = 2, max = 32, message = "Наименование должно содержать от 2 до 32 символов")
    private String title;

}
