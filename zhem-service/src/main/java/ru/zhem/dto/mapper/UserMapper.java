package ru.zhem.dto.mapper;

import org.springframework.stereotype.Component;
import ru.zhem.dto.ZhemUserAuthDto;
import ru.zhem.dto.ZhemUserCreationDto;
import ru.zhem.dto.ZhemUserDto;
import ru.zhem.dto.ZhemUserUpdateDto;
import ru.zhem.entity.ZhemUser;

@Component
public class UserMapper {

    public ZhemUser fromCreationDto(ZhemUserCreationDto userDto) {
        return ZhemUser.builder()
                .phone(userDto.getPhone())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .roles(userDto.getRoles())
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
                .roles(user.getRoles())
                .build();
    }

}
