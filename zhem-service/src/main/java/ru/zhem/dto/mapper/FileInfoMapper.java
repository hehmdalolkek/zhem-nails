package ru.zhem.dto.mapper;

import org.springframework.stereotype.Component;
import ru.zhem.dto.response.FileInfoDto;
import ru.zhem.entity.FileInfo;

@Component
public class FileInfoMapper {

    public FileInfoDto fromEntity(FileInfo fileInfo) {
        return FileInfoDto.builder()
                .id(fileInfo.getId())
                .name(fileInfo.getName())
                .type(fileInfo.getType())
                .createdAt(fileInfo.getCreatedAt())
                .updatedAt(fileInfo.getUpdatedAt())
                .build();
    }

}
