package ru.zhem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "zhem", name = "t_appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @NotNull
    @OneToOne
    @JoinColumn(name = "id_workinterval")
    private WorkInterval workInterval;

    @CreationTimestamp
    @Column(name = "c_created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "c_updated_at")
    private LocalDateTime updatedAt;

}
