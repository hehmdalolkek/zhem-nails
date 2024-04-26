package ru.zhem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.entity.FileType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZhemFileDto {

    private Long id;

    private String path;

    private FileType type;

}
