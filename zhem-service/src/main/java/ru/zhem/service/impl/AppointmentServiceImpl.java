package ru.zhem.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zhem.entity.*;
import ru.zhem.exception.*;
import ru.zhem.repository.AppointmentRepository;
import ru.zhem.repository.IntervalRepository;
import ru.zhem.repository.ZhemServiceRepository;
import ru.zhem.repository.ZhemUserRepository;
import ru.zhem.service.interfaces.AppointmentService;

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
    public Page<Appointment> findAllAppointmentsByUserId(long userId, Pageable pageable) {
        boolean isExists = this.zhemUserRepository.existsById(userId);
        if (!isExists) {
            throw new ZhemUserNotFoundException("User not found");
        }
        return this.appointmentRepository.findAllByUserIdOrderByIntervalDateDescIntervalTimeDesc(userId, pageable);
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

        List<ZhemService> services = this.serviceRepository.findAllById(appointment.getServices().stream()
                .map(ZhemService::getId)
                .collect(Collectors.toSet()));
        if (appointment.getServices().size() != services.size()) {
            throw new ZhemServiceNotFoundException("Service not found");
        }
        appointment.setServices(new HashSet<>(services));

        appointment.setUser(foundedUser);
        foundedInterval.setStatus(Status.BOOKED);
        appointment.setInterval(foundedInterval);

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

        if (Objects.nonNull(appointment.getUser().getId())) {
            ZhemUser foundedUser = this.zhemUserRepository.findById(appointment.getUser().getId())
                    .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
            foundedAppointment.setUser(foundedUser);
        }
        if (Objects.nonNull(appointment.getInterval().getId())) {
            Interval foundedInterval = this.intervalRepository.findById(appointment.getInterval().getId())
                    .orElseThrow(() -> new IntervalNotFoundException("Interval not found"));
            if (foundedInterval.getStatus() == Status.BOOKED
                    && !Objects.equals(foundedAppointment.getInterval().getId(), foundedInterval.getId())) {
                throw new IntervalIsBookedException("Interval is already booked");
            }
            foundedAppointment.getInterval().setStatus(Status.AVAILABLE);
            foundedInterval.setStatus(Status.BOOKED);
            foundedAppointment.setInterval(foundedInterval);
        }

        if (!appointment.getServices().isEmpty()) {
            List<ZhemService> services = this.serviceRepository.findAllById(appointment.getServices().stream()
                    .map(ZhemService::getId)
                    .collect(Collectors.toSet()));
            if (appointment.getServices().size() != services.size()) {
                throw new ZhemServiceNotFoundException("Service not found");
            }
            foundedAppointment.setServices(new HashSet<>(services));
        }

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
