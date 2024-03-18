package ru.zhem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "zhem", name = "t_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "c_phone", unique = true)
    private BigDecimal phone;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_surname")
    private String surname;

    @CreationTimestamp
    @Column(name = "c_created_at")
    private LocalDateTime createdAt;

}
