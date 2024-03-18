package ru.zhem.service;

import ru.zhem.entity.Appointment;
import ru.zhem.entity.User;
import ru.zhem.entity.WorkInterval;

import java.util.Optional;

public interface AppointmentService {

    Iterable<Appointment> findAllAppointments();

    Iterable<Appointment> findAppointmentsByUserId(long userId);

    Optional<Appointment> findAppointment();

    Appointment createAppointment(User user, WorkInterval workInterval);

    void updateAppointment(Long appointmentId, User user, WorkInterval workInterval);

    void deleteAppointment(long appointmentId);

}
