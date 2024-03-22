package ru.zhem.service;

import ru.zhem.entity.Appointment;

import java.util.Optional;

public interface AppointmentService {

    Iterable<Appointment> findAllAppointments();

    Optional<Appointment> findAppointment(long appointmentId);

    Appointment createAppointment(Long userId, Long workIntervalId);

    void updateAppointment(long appointmentId, Long userId, Long workIntervalId);

    void deleteAppointment(long appointmentId);

}
