package ru.zhem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @NotNull
    @Column(name = "c_date")
    private LocalDate date;

    @NotNull
    @Column(name = "c_start_time")
    private LocalTime startTime;

    @NotNull
    @Column(name = "c_is_booked")
    private Boolean isBooked = false;

}
