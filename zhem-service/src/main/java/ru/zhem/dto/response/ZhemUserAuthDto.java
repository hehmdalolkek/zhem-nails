package ru.zhem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZhemUserAuthDto {

    private Long id;

    private String phone;

    private String password;

    private Set<RoleDto> roles;

}
