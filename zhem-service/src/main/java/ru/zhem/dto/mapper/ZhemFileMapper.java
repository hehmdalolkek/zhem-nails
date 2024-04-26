package ru.zhem.dto.mapper;

import org.springframework.stereotype.Component;
import ru.zhem.dto.response.ZhemFileDto;
import ru.zhem.entity.ZhemFile;

@Component
public class ZhemFileMapper {

    public ZhemFileDto fromEntity(ZhemFile file) {
        return ZhemFileDto.builder()
                .id(file.getId())
                .path(file.getPath())
                .type(file.getType())
                .build();
    }

}
