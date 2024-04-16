package ru.zhem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.entity.*;
import ru.zhem.exception.*;
import ru.zhem.repository.AppointmentRepository;
import ru.zhem.repository.IntervalRepository;
import ru.zhem.repository.ZhemServiceRepository;
import ru.zhem.repository.ZhemUserRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final ZhemUserRepository zhemUserRepository;

    private final IntervalRepository intervalRepository;

    private final ZhemServiceRepository serviceRepository;

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
        if (foundedInterval.getStatus() == IntervalStatus.BOOKED) {
            throw new IntervalIsBookedException("Interval is already booked");
        }

        List<ZhemService> services = this.serviceRepository.findAllById(appointment.getServices().stream()
                .map(ZhemService::getId)
                .collect(Collectors.toSet()));
        if (appointment.getServices().size() != services.size()) {
            throw new ZhemServiceNotFoundException("Service not found");
        }
        appointment.setServices(new HashSet<>(services));

        appointment.setUser(foundedUser);
        foundedInterval.setStatus(IntervalStatus.BOOKED);
        appointment.setInterval(foundedInterval);
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        if (Objects.nonNull(appointment.getDetails()) && appointment.getDetails().isBlank()) {
            appointment.setDetails(null);
        }

        return this.appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public Appointment updateAppointment(long appointmentId, Appointment appointment) {
        Appointment foundedAppointment = this.appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        if (foundedAppointment.getStatus() == AppointmentStatus.CANCELED) {
            throw new AppointmentCanceled("Appointment is already canceled");
        }

        if (Objects.nonNull(appointment.getUser().getId())) {
            ZhemUser foundedUser = this.zhemUserRepository.findById(appointment.getUser().getId())
                    .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
            foundedAppointment.setUser(foundedUser);
        }
        if (Objects.nonNull(appointment.getInterval().getId())) {
            Interval foundedInterval = this.intervalRepository.findById(appointment.getInterval().getId())
                    .orElseThrow(() -> new IntervalNotFoundException("Interval not found"));
            if (foundedInterval.getStatus() == IntervalStatus.BOOKED
                    && !Objects.equals(foundedAppointment.getInterval().getId(), foundedInterval.getId())) {
                throw new IntervalIsBookedException("Interval is already booked");
            }
            foundedAppointment.getInterval().setStatus(IntervalStatus.AVAILABLE);
            foundedInterval.setStatus(IntervalStatus.BOOKED);
            foundedAppointment.setInterval(foundedInterval);
        }
        // TODO ДОБАВИТЬ ПРОВЕРКУ СУЩЕСТВОВАНИЯ ВСЕХ УСЛУГ!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // TODO ДОБАВИТЬ ПРОВЕРКУ СУЩЕСТВОВАНИЯ ВСЕХ УСЛУГ!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (Objects.nonNull(appointment.getDetails()) && !appointment.getDetails().isBlank()) {
            foundedAppointment.setDetails(appointment.getDetails());
        }

        return this.appointmentRepository.save(foundedAppointment);
    }

    @Override
    @Transactional
    public void deleteAppointment(long appointmentId) {
        Appointment foundedAppointment = this.appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        if (foundedAppointment.getStatus() == AppointmentStatus.CANCELED) {
            throw new AppointmentCanceled("Appointment is already canceled");
        }
        foundedAppointment.getInterval().setStatus(IntervalStatus.AVAILABLE);
        foundedAppointment.setStatus(AppointmentStatus.CANCELED);
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
