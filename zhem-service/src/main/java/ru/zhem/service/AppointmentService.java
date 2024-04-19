package ru.zhem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.zhem.entity.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AppointmentService {

    Page<Appointment> findAllAppointmentsByUserId(long userId, Pageable pageable);

    Map<LocalDate, List<Appointment>> findAllAppointmentsByIntervalDate(Integer year, Integer month);

    Appointment findAppointmentById(long intervalId);

    Appointment createAppointment(Appointment appointment);

    Appointment updateAppointment(long appointmentId, Appointment appointment);

    void deleteAppointment(long appointmentId);

    Appointment findAppointmentByIntervalId(long intervalId);
}
