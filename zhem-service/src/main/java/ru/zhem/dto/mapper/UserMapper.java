package ru.zhem.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.zhem.dto.response.ZhemUserAuthDto;
import ru.zhem.dto.request.ZhemUserCreationDto;
import ru.zhem.dto.response.ZhemUserDto;
import ru.zhem.dto.request.ZhemUserUpdateDto;
import ru.zhem.entity.ZhemUser;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    public ZhemUser fromCreationDto(ZhemUserCreationDto userDto) {
        return ZhemUser.builder()
                .phone(userDto.getPhone())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .roles(userDto.getRoles().stream()
                        .map(this.roleMapper::fromDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    public ZhemUser fromUpdateDto(ZhemUserUpdateDto userDto) {
        return ZhemUser.builder()
                .phone(userDto.getPhone())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .build();
    }

    public ZhemUserDto fromEntity(ZhemUser user) {
        return ZhemUserDto.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public ZhemUserAuthDto fromEntityForAuth(ZhemUser user) {
        return ZhemUserAuthDto.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(this.roleMapper::fromEntity)
                        .collect(Collectors.toSet()))
                .build();
    }

}
