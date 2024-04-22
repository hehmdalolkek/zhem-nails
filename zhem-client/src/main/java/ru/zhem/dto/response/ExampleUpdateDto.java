package ru.zhem.dto.response;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExampleUpdateDto {

    @Size(max = 32, message = "Наименование должно быть до 32 символов")
    private String title;

    private MultipartFile image;

}
