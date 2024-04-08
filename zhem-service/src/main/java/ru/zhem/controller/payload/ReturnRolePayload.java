package ru.zhem.controller.payload;

import lombok.Builder;
import lombok.Data;
import ru.zhem.entity.Role;

@Data
@Builder
public class ReturnRolePayload {

    private Integer id;

    private String title;

    public static ReturnRolePayload fromEntity(Role role) {
        return ReturnRolePayload.builder()
                .id(role.getId())
                .title(role.getTitle())
                .build();
    }

}
