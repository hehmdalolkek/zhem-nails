package ru.zhem.dto.mapper;

import org.springframework.stereotype.Component;
import ru.zhem.dto.request.RoleCreationDto;
import ru.zhem.dto.response.RoleDto;
import ru.zhem.entity.Role;

@Component
public class RoleMapper {

    public Role fromCreationDto(RoleCreationDto roleDto) {
        return Role.builder()
                .title(roleDto.getTitle())
                .build();
    }

    public Role fromDto(RoleDto roleDto) {
        return Role.builder()
                .title(roleDto.getTitle())
                .build();
    }

    public RoleDto fromEntity(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .title(role.getTitle())
                .build();
    }

}
