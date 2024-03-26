package ru.zhem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.zhem.entity.constraints.CheckPhoneNumber;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "zhem", name = "t_clients")
public class Client {

    @Id
    @CheckPhoneNumber
    @Column(name = "c_phone", unique = true)
    private BigDecimal phone;

    @NotBlank
    @Size(min = 2, max = 32)
    @Column(name = "c_name")
    private String name;

    @Size(min = 2, max = 32)
    @Column(name = "c_surname")
    private String surname;

    @NotBlank
    @Column(name = "c_password")
    private String password;

    @CreationTimestamp
    @Column(name = "c_created_at")
    private LocalDateTime createdAt;

}
