package ru.zhem.controller.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.entity.Role;
import ru.zhem.entity.ZhemUser;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnZhemUserPayload {

    private Long id;

    private String phone;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private Set<Role> roles;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ReturnZhemUserPayload fromEntity(ZhemUser user) {
        return ReturnZhemUserPayload.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .password(user.getPassword())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
