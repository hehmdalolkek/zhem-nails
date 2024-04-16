package ru.zhem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZhemUserAuthDto {

    private Long id;

    private String phone;

    private String password;

    private Set<RoleDto> roles;

}
