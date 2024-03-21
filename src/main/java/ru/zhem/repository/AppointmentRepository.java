package ru.zhem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.Appointment;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

    Iterable<Appointment> findAllByUserId(long userId);
}
