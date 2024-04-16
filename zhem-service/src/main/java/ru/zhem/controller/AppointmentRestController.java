package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.zhem.dto.mapper.AppointmentMapper;
import ru.zhem.dto.request.AppointmentCreationDto;
import ru.zhem.dto.request.AppointmentUpdateDto;
import ru.zhem.dto.response.AppointmentDto;
import ru.zhem.dto.response.DailyAppointmentDto;
import ru.zhem.entity.Appointment;
import ru.zhem.exception.*;
import ru.zhem.service.AppointmentService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        Map<LocalDate, List<Appointment>> appointments =
                this.appointmentService.findAllAppointmentsByIntervalDate(year, month);
        List<DailyAppointmentDto> appointmentDtos = appointments.entrySet().stream()
                .map(this.appointmentMapper::fromEntryOfEntity)
                .toList();

        return ResponseEntity.ok()
                .body(appointmentDtos);
    }

    @GetMapping("/appointment/{appointmentId:\\d+}")
    public ResponseEntity<?> findAppointmentById(@PathVariable("appointmentId") long appointmentId) {
        try {
            Appointment foundedAppointment = this.appointmentService.findAppointmentById(appointmentId);
            return ResponseEntity.ok()
                    .body(this.appointmentMapper.fromEntity(foundedAppointment));
        } catch (AppointmentNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    @GetMapping("/appointment/interval/{intervalId:\\d+}")
    public ResponseEntity<?> findAppointmentByInterval(@PathVariable("intervalId") long intervalId) {
        try {
            Appointment foundedAppointment = this.appointmentService.findAppointmentByIntervalId(intervalId);
            return ResponseEntity.ok()
                    .body(this.appointmentMapper.fromEntity(foundedAppointment));
        } catch (AppointmentNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        } catch (IntervalNotFoundException exception) {
            throw new BadRequestException(exception.getMessage());
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
            } catch (ZhemUserNotFoundException | IntervalNotFoundException | ZhemServiceNotFoundException exception) {
                throw new BadRequestException(exception.getMessage());
            } catch (IntervalIsBookedException exception) {
                bindingResult.addError(
                        new FieldError("Appointment", "interval", "Выбранный интервал уже забронирован"));
                throw new BindException(bindingResult);
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
            } catch (ZhemUserNotFoundException | IntervalNotFoundException | AppointmentNotFoundException
                    | AppointmentCanceled | ZhemServiceNotFoundException exception) {
                throw new BadRequestException(exception.getMessage());
            } catch (IntervalIsBookedException exception) {
                bindingResult.addError(
                        new FieldError("Appointment", "interval", "Выбранный интервал уже забронирован"));
                throw new BindException(bindingResult);
            }
        }
    }

    @DeleteMapping("/appointment/{appointmentId:\\d+}")
    public ResponseEntity<?> deleteAppointmentById(@PathVariable("appointmentId") long appointmentId) {
        try {
            this.appointmentService.deleteAppointment(appointmentId);
            return ResponseEntity.ok()
                    .build();
        } catch (AppointmentNotFoundException | AppointmentCanceled exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

}
