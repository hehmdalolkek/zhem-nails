package ru.zhem.repository;

import org.springframework.data.repository.CrudRepository;
import ru.zhem.entity.Appointment;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
}
