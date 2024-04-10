package ru.zhem.service;

import ru.zhem.entity.Appointment;
import ru.zhem.entity.ZhemUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AppointmentService {

    List<Appointment> findAllAppointmentsByUserId(long userId);

    Map<LocalDate, List<Appointment>> findAllAppointmentsByIntervalDate(Integer year, Integer month);

    Appointment findAppointmentById(long intervalId);

    Appointment createAppointment(Appointment appointment);

    Appointment updateAppointment(long appointmentId, Appointment appointment);

    void deleteAppointment(long appointmentId);

}
