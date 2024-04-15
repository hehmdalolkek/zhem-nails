package ru.zhem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.entity.Appointment;
import ru.zhem.entity.Interval;
import ru.zhem.entity.Status;
import ru.zhem.entity.ZhemUser;
import ru.zhem.exception.AppointmentNotFoundException;
import ru.zhem.exception.IntervalIsBookedException;
import ru.zhem.exception.IntervalNotFoundException;
import ru.zhem.exception.ZhemUserNotFoundException;
import ru.zhem.repository.AppointmentRepository;
import ru.zhem.repository.IntervalRepository;
import ru.zhem.repository.ZhemUserRepository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final ZhemUserRepository zhemUserRepository;

    private final IntervalRepository intervalRepository;

    @Override
    @Transactional
    public List<Appointment> findAllAppointmentsByUserId(long userId) {
        boolean isExists = this.zhemUserRepository.existsById(userId);
        if (!isExists) {
            throw new ZhemUserNotFoundException("User not found");
        }
        return this.appointmentRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public Map<LocalDate, List<Appointment>> findAllAppointmentsByIntervalDate(Integer year, Integer month) {
        List<Appointment> appointments = this.appointmentRepository.findAllByIntervalDate(year, month);
        return appointments.stream().collect(
                Collectors.groupingBy(appointment -> appointment.getInterval().getDate(),
                LinkedHashMap::new, Collectors.toList()));
    }

    @Override
    @Transactional
    public Appointment findAppointmentById(long appointmentId) {
        return this.appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
    }

    @Override
    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        ZhemUser foundedUser = this.zhemUserRepository.findById(appointment.getUser().getId())
                .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
        Interval foundedInterval = this.intervalRepository.findById(appointment.getInterval().getId())
                .orElseThrow(() -> new IntervalNotFoundException("Interval not found"));
        if (foundedInterval.getStatus() == Status.BOOKED) {
            throw new IntervalIsBookedException("Interval is already booked");
        }
        appointment.setUser(foundedUser);
        foundedInterval.setStatus(Status.BOOKED);
        appointment.setInterval(foundedInterval);

        if (Objects.nonNull(appointment.getDetails()) && appointment.getDetails().isBlank()) {
            appointment.setDetails(null);
        } else {
            appointment.setDetails(appointment.getDetails().strip());
        }

        return this.appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public Appointment updateAppointment(long appointmentId, Appointment appointment) {
        Appointment foundedAppointment = this.appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        if (Objects.nonNull(appointment.getUser().getId())) {
            ZhemUser foundedUser = this.zhemUserRepository.findById(appointment.getUser().getId())
                    .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
            foundedAppointment.setUser(foundedUser);
        }
        if (Objects.nonNull(appointment.getInterval().getId())) {
            Interval foundedInterval = this.intervalRepository.findById(appointment.getInterval().getId())
                    .orElseThrow(() -> new IntervalNotFoundException("Interval not found"));
            if (foundedInterval.getStatus() == Status.BOOKED) {
                throw new IntervalIsBookedException("Interval is already booked");
            }
            foundedAppointment.getInterval().setStatus(Status.AVAILABLE);
            foundedInterval.setStatus(Status.BOOKED);
            foundedAppointment.setInterval(foundedInterval);
        }
        if (Objects.nonNull(appointment.getDetails()) && !appointment.getDetails().isBlank()) {
            foundedAppointment.setDetails(appointment.getDetails().strip());
        }

        return this.appointmentRepository.save(foundedAppointment);
    }

    @Override
    @Transactional
    public void deleteAppointment(long appointmentId) {
        Appointment foundedAppointment = this.appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        foundedAppointment.getInterval().setStatus(Status.AVAILABLE);
        this.appointmentRepository.deleteById(appointmentId);
    }

    @Override
    public Appointment findAppointmentByIntervalId(long intervalId) {
        boolean isExists = this.intervalRepository.existsById(intervalId);
        if (isExists) {
            return this.appointmentRepository.findByIntervalId(intervalId)
                    .orElseThrow(() -> new AppointmentNotFoundException("Interval not found"));
        } else {
            throw new IntervalNotFoundException("Interval not found");
        }

    }
}
