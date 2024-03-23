package ru.zhem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.entity.Appointment;
import ru.zhem.entity.User;
import ru.zhem.entity.WorkInterval;
import ru.zhem.repository.AppointmentRepository;
import ru.zhem.repository.UserRepository;
import ru.zhem.repository.WorkIntervalRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final UserRepository userRepository;

    private final WorkIntervalRepository workIntervalRepository;

    @Override
    @Transactional
    public Iterable<Appointment> findAllAppointments() {
        return this.appointmentRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Appointment> findAppointment(long appointmentId) {
        return this.appointmentRepository.findById(appointmentId);
    }

    @Override
    @Transactional
    public Appointment createAppointment(Long userId, Long workIntervalId, String details) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User is not found"));
        WorkInterval workInterval = this.workIntervalRepository.findById(workIntervalId)
                .orElseThrow(() -> new NoSuchElementException("Work interval is not found"));
        if (workInterval.getIsBooked().equals(true)) {
            throw new IllegalStateException("Work interval is already booked");
        }

        workInterval.setIsBooked(true);
        return this.appointmentRepository.save(
                new Appointment(null, user, workInterval,
                        details.isBlank() ? null : details, null, null)
        );
    }

    @Override
    @Transactional
    public void updateAppointment(long appointmentId, Long userId, Long workIntervalId, String details) {
        this.appointmentRepository.findById(appointmentId)
                .ifPresentOrElse((appointment) -> {
                    if (userId != null) {
                        User user = this.userRepository.findById(userId)
                                .orElseThrow(() -> new NoSuchElementException("User is not found"));
                        appointment.setUser(user);
                    }
                    if (workIntervalId != null){
                        WorkInterval workInterval = this.workIntervalRepository.findById(workIntervalId)
                                .orElseThrow(() -> new NoSuchElementException("Work interval is not found"));
                        if (workInterval.getIsBooked().equals(true)) {
                            throw new IllegalStateException("Work interval is already booked");
                        }
                        workInterval.setIsBooked(true);
                        appointment.getWorkInterval().setIsBooked(false);
                        appointment.setWorkInterval(workInterval);
                    }
                    if (details != null && !details.isBlank()) {
                        appointment.setDetails(details);
                    }
                }, () -> {
                    throw new NoSuchElementException("Appointment is not found");
                });
    }

    @Override
    @Transactional
    public void deleteAppointment(long appointmentId) {
        this.appointmentRepository.findById(appointmentId).
                ifPresentOrElse(appointment -> {
                    appointment.getWorkInterval().setIsBooked(false);
                }, () -> { throw new NoSuchElementException("Appointment is not found"); });
        this.appointmentRepository.deleteById(appointmentId);
    }
}
