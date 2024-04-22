package ru.zhem.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.zhem.dto.constraints.MultipartFileNotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExampleCreationDto {

    @NotBlank(message = "Наименование должно быть заполнено")
    @Size(max = 32, message = "Наименование должно быть до 32 символов")
    private String title;

    @MultipartFileNotNull(message = "Вложите изображение")
    private MultipartFile image;

}
