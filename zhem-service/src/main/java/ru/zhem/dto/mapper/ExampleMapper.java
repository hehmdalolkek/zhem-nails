package ru.zhem.dto.mapper;

import org.springframework.stereotype.Component;
import ru.zhem.dto.request.ExampleCreationDto;
import ru.zhem.dto.request.ExampleUpdateDto;
import ru.zhem.dto.response.ExampleDto;
import ru.zhem.entity.Example;

@Component
public class ExampleMapper {

    public Example fromCreationDto(ExampleCreationDto exampleDto, String fileName) {
        return Example.builder()
                .title(exampleDto.getTitle())
                .fileName(fileName)
                .build();
    }

    public Example fromUpdateDto(ExampleUpdateDto exampleDto, String fileName) {
        return Example.builder()
                .title(exampleDto.getTitle())
                .fileName(fileName)
                .build();
    }

    public ExampleDto fromEntity(Example example) {
        return ExampleDto.builder()
                .id(example.getId())
                .title(example.getTitle())
                .fileName(example.getFileName())
                .createdAt(example.getCreatedAt())
                .updatedAt(example.getUpdatedAt())
                .build();
    }


}
