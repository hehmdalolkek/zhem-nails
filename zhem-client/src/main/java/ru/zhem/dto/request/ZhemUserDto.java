package ru.zhem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZhemUserDto {

    private Long id;

    private String phone;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
