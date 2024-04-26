package ru.zhem.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreationDto {

    @Size(min = 1, max = 256, message = "Поле должно быть от 1 до 256 символов")
    private String content;

}
