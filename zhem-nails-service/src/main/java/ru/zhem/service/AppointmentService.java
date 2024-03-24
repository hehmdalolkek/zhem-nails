package ru.zhem.service;

import ru.zhem.entity.Appointment;

import java.math.BigDecimal;
import java.util.Optional;

public interface AppointmentService {

    Iterable<Appointment> findAllAppointments();

    Optional<Appointment> findAppointment(long appointmentId);

    Appointment createAppointment(BigDecimal userPhone, Long workIntervalId, String details);

    void updateAppointment(long appointmentId, BigDecimal userPhone, Long workIntervalId, String details);

    void deleteAppointment(long appointmentId);

}
