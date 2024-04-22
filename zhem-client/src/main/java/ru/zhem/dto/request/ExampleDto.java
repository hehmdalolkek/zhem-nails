package ru.zhem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
