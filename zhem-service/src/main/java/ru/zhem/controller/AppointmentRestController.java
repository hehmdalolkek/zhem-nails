package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zhem.dto.AppointmentCreationDto;
import ru.zhem.dto.AppointmentDto;
import ru.zhem.dto.AppointmentUpdateDto;
import ru.zhem.dto.mapper.AppointmentMapper;
import ru.zhem.entity.Appointment;
import ru.zhem.exception.*;
import ru.zhem.service.AppointmentService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/appointments")
public class AppointmentRestController {

    private final AppointmentService appointmentService;

    private final AppointmentMapper appointmentMapper;

    @GetMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> findAllAppointmentsByUser(@PathVariable("userId") long userId) {
        try {
            List<Appointment> foundedAppointments = this.appointmentService.findAllAppointmentsByUserId(userId);
            List<AppointmentDto> appointmentDtos = foundedAppointments.stream()
                    .map(this.appointmentMapper::fromEntity)
                    .toList();
            return ResponseEntity.ok()
                    .body(appointmentDtos);
        } catch (ZhemUserNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findAllAppointments(@RequestParam("year") Integer year,
                                                 @RequestParam("month") Integer month) {
        return ResponseEntity.ok()
                .body(this.appointmentService.findAllAppointmentsByIntervalDate(year, month));
    }

    @GetMapping("/appointment/{appointmentId:\\d+}")
    public ResponseEntity<?> findAppointmentById(@PathVariable("appointmentId") long appointmentId) {
        try {
            Appointment foundedAppointment = this.appointmentService.findAppointmentById(appointmentId);
            return ResponseEntity.ok()
                    .body(this.appointmentMapper.fromEntity(foundedAppointment));
        } catch (ZhemUserNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@Valid @RequestBody AppointmentCreationDto appointmentDto,
                                               BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                Appointment createdAppointment = this.appointmentService
                        .createAppointment(this.appointmentMapper.fromCreationDto(appointmentDto));
                return ResponseEntity.created(URI.create("/service-api/v1/appointments/appointment/" + createdAppointment.getId()))
                        .body(this.appointmentMapper.fromEntity(createdAppointment));
            } catch (ZhemUserNotFoundException | IntervalNotFoundException | IntervalIsBookedException exception) {
                throw new BadRequestException(exception.getMessage());
            }
        }
    }

    @PatchMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> updateAppointment(@PathVariable("appointmentId") long appointmentId,
                                               @Valid @RequestBody AppointmentUpdateDto appointmentDto,
                                               BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                Appointment updatedAppointment = this.appointmentService
                        .updateAppointment(appointmentId,this.appointmentMapper.fromUpdateDto(appointmentDto));
                return ResponseEntity.ok()
                        .body(this.appointmentMapper.fromEntity(updatedAppointment));
            } catch (ZhemUserNotFoundException | IntervalNotFoundException | IntervalIsBookedException exception) {
                throw new BadRequestException(exception.getMessage());
            }
        }
    }

    @DeleteMapping("/appointment/{appointmentId:\\d+}")
    public ResponseEntity<?> deleteAppointmentById(@PathVariable("appointmentId") long appointmentId) {
        try {
            this.appointmentService.deleteAppointment(appointmentId);
            return ResponseEntity.ok()
                    .build();
        } catch (ZhemUserNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

}
