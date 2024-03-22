package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.zhem.controller.payload.NewAppointmentPayload;
import ru.zhem.controller.payload.UpdateAppointmentPayload;
import ru.zhem.entity.Appointment;
import ru.zhem.service.AppointmentService;

import java.util.Map;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentRestController {

    private final AppointmentService appointmentService;

    @GetMapping
    public Iterable<Appointment> findAppointments() {
        return this.appointmentService.findAllAppointments();
    }

    @GetMapping("/{appointmentId:\\d+}")
    public Appointment findAppointment(@PathVariable("appointmentId") long appointmentId) {
        return this.appointmentService.findAppointment(appointmentId)
                .orElseThrow(NoSuchElementException::new);
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@Valid @RequestBody NewAppointmentPayload payload,
                                               BindingResult bindingResult,
                                               UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Appointment appointment = this.appointmentService.createAppointment(payload.userId(), payload.workIntervalId());
            return ResponseEntity.created(
                    uriComponentsBuilder
                            .replacePath("/api/v1/appointments/{appointmentId}")
                            .build(Map.of("appointmentId", appointment.getId()))
            ).body(appointment);
        }
    }

    @PatchMapping("/{appointmentId:\\d+}")
    public ResponseEntity<?> updateAppointment(@PathVariable("appointmentId") long appointmentId,
                                               @Valid @RequestBody UpdateAppointmentPayload payload,
                                               BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.appointmentService.updateAppointment(appointmentId, payload.userId(), payload.workIntervalId());
            return ResponseEntity.noContent()
                    .build();
        }
    }

    @DeleteMapping("/{appointmentId:\\d+}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("appointmentId") long appointmentId) {
        this.appointmentService.findAppointment(appointmentId)
                .orElseThrow(NoSuchElementException::new);
        this.appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.noContent()
                .build();
    }

}
