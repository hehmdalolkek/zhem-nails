package ru.zhem.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.zhem.common.constraint.MultipartFileNotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreationDto {

    @Size(min = 1, max = 256, message = "Описание должно содержать от 1 до 256 символов")
    private String content;

    @MultipartFileNotNull(message = "Изображение должно быть приложено")
    private MultipartFile image;

}
