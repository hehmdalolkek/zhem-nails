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

    @Size(max = 256, message = "Поле должно быть до 256 символов")
    private String content;

    @MultipartFileNotNull(message = "Вложите изображение")
    private MultipartFile image;

}
