package ru.zhem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.Appointment;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @EntityGraph(value = "appointment_entity-graph")
    @Override
    Optional<Appointment> findById(Long appointmentId);

    Page<Appointment> findAllByUserIdOrderByIntervalDateDescIntervalTimeDesc(Long userId, Pageable pageable);

    @EntityGraph(value = "appointment_entity-graph")
    @Query("from Appointment i where extract(year from i.interval.date) = :year and extract(month from i.interval.date) = :month" +
            " order by i.interval.date, i.interval.time")
    List<Appointment> findAllByIntervalDate(@Param("year") Integer year, @Param("month") Integer month);

    @EntityGraph(value = "appointment_entity-graph")
    Optional<Appointment> findByIntervalId(long intervalId);
}
