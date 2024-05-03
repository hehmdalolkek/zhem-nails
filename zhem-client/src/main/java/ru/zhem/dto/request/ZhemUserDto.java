package ru.zhem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public String getFormattedPhone() {
        return  "+" +
                this.phone.charAt(0) +
                "(" +
                this.phone.substring(1, 4) +
                ")" +
                this.phone.substring(4, 7) +
                "-" +
                this.phone.substring(7, 9) +
                "-" +
                this.phone.substring(9, 11);
    }

}
