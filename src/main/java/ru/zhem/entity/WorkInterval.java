package ru.zhem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "zhem", name = "t_workintervals")
public class WorkInterval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_workday", referencedColumnName = "id")
    private WorkDay workDay;

    @Column(name = "c_start_time")
    private LocalTime startTime;

    @Column(name = "c_is_booked")
    private Boolean isBooked = false    ;

}
