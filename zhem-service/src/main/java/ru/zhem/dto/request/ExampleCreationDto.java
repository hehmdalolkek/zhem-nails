package ru.zhem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExampleCreationDto {

    @NotBlank
    @Size(min = 2, max = 32, message = "Наименование должно быть от 2 до 32 символов")
    private String title;

    @NotNull
    private MultipartFile image;

}
