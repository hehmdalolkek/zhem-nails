package ru.zhem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZhemUserAuthDto {

    private Long id;

    private String phone;

    private String password;

    private Set<RoleDto> roles;

    @ToString.Include(name = "phone")
    public String getFormattedPhone() {
        return this.phone == null || this.phone.isBlank() ? null
                : "+" +
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

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "[PROTECTED]";
    }

}
