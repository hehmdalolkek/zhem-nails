package ru.zhem.dto.response;

import jakarta.persistence.*;
import lombok.*;
import ru.zhem.entity.BaseEntity;
import ru.zhem.entity.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDto {

    private Long id;

    private String name;

    private String type;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}