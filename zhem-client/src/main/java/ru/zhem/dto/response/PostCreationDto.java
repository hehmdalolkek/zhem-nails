package ru.zhem.dto.response;

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

    @Size(max = 256, message = "Описание должно содержать до 256 символов")
    private String content;

    @MultipartFileNotNull(message = "Изображение должно быть приложено")
    private MultipartFile image;

}
