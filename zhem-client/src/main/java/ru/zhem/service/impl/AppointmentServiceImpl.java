package ru.zhem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.zhem.client.interfaces.AppointmentRestClient;
import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.DailyAppointmentDto;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.dto.response.AppointmentUpdateDto;
import ru.zhem.exceptions.AppointmentNotFoundException;
import ru.zhem.service.interfaces.AppointmentService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRestClient appointmentRestClient;

    @Override
    public Page<AppointmentDto> findAllAppointmentsByUser(long userId, Pageable pageable) {
        return this.appointmentRestClient.findAllAppointmentsByUser(userId, pageable);
    }

    @Override
    public List<DailyAppointmentDto> findAllAppointments(int year, int month) {
        return null;
    }

    @Override
    public AppointmentDto findAppointmentById(long appointmentId) {
        return this.appointmentRestClient.findAppointmentById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
    }

    @Override
    public void createAppointment(AppointmentCreationDto appointmentDto) {
        if (appointmentDto.getDetails().isBlank()) {
            appointmentDto.setDetails(null);
        }
        this.appointmentRestClient.createAppointment(appointmentDto);
    }

    @Override
    public void updateAppointment(long appointmentId, AppointmentUpdateDto appointmentDto) {
        if (appointmentDto.getDetails().isBlank()) {
            appointmentDto.setDetails(null);
        }
        this.appointmentRestClient.updateAppointment(appointmentId, appointmentDto);
    }

    @Override
    public void deleteAppointment(long appointmentId) {
        this.appointmentRestClient.deleteAppointment(appointmentId);
    }

    @Override
    public AppointmentDto findAppointmentByIntervalId(long intervalId) {
        return this.appointmentRestClient.findAppointmentByInterval(intervalId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
    }
}
