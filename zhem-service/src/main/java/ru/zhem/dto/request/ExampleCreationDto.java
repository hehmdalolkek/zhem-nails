package ru.zhem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.zhem.dto.request.constraint.MultipartFileNotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExampleCreationDto {

    @NotBlank(message = "Наименование должно быть заполнено")
    @Size(min = 2, max = 32, message = "Наименование должно быть от 2 до 32 символов")
    private String title;

    @MultipartFileNotNull(message = "Вложите изображение")
    private MultipartFile image;

}
