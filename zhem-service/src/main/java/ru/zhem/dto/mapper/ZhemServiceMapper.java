package ru.zhem.dto.mapper;

import org.springframework.stereotype.Component;
import ru.zhem.dto.request.ZhemServiceCreationDto;
import ru.zhem.dto.response.ZhemServiceDto;
import ru.zhem.entity.ZhemService;

@Component
public class ZhemServiceMapper {
    public ZhemServiceDto fromEntity(ZhemService service) {
        return ZhemServiceDto.builder()
                .id(service.getId())
                .title(service.getTitle())
                .build();
    }

    public ZhemService fromCreationDto(ZhemServiceCreationDto serviceDto) {
        return ZhemService.builder()
                .title(serviceDto.getTitle())
                .build();
    }

    public ZhemService fromId(Integer serviceId) {
        return ZhemService.builder()
                .id(serviceId)
                .build();
    }
}
