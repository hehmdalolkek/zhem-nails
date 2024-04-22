package ru.zhem.dto.response;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.zhem.entity.BaseEntity;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExampleDto {

    private Long id;

    private String title;

    private String fileName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
