package ru.zhem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zhem.common.annotation.LogAnnotation;
import ru.zhem.dto.mapper.AppointmentMapper;
import ru.zhem.dto.request.AppointmentCreationDto;
import ru.zhem.dto.request.AppointmentUpdateDto;
import ru.zhem.dto.response.AppointmentDto;
import ru.zhem.dto.response.DailyAppointmentDto;
import ru.zhem.entity.Appointment;
import ru.zhem.service.interfaces.AppointmentService;

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


    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Page of appointments by user",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Page.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Appointment", operation = "Get all appointments by user")
    @GetMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> findAllAppointmentsByUser(@PathVariable("userId") long userId,
                                                       @ParameterObject Pageable pageable) {
        Page<Appointment> foundedAppointments =
                this.appointmentService.findAllAppointmentsByUserId(userId, pageable);
        Page<AppointmentDto> appointmentDtos = foundedAppointments.map(this.appointmentMapper::fromEntity);
        return ResponseEntity.ok()
                .body(appointmentDtos);
    }


    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = DailyAppointmentDto.class
                                            )
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Year/month is invalid",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Appointment", operation = "Get all appointments")
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


    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find appointment by id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = AppointmentDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Appointment not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Appointment", operation = "Get appointment by id")
    @GetMapping("/appointment/{appointmentId:\\d+}")
    public ResponseEntity<?> findAppointmentById(@PathVariable("appointmentId") long appointmentId) {
        Appointment foundedAppointment = this.appointmentService.findAppointmentById(appointmentId);
        return ResponseEntity.ok()
                .body(this.appointmentMapper.fromEntity(foundedAppointment));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find appointment by interval id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = AppointmentDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Appointment not found by interval id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Appointment", operation = "Get appointment by interval id")
    @GetMapping("/appointment/interval/{intervalId:\\d+}")
    public ResponseEntity<?> findAppointmentByInterval(@PathVariable("intervalId") long intervalId) {
        Appointment foundedAppointment = this.appointmentService.findAppointmentByIntervalId(intervalId);
        return ResponseEntity.ok()
                .body(this.appointmentMapper.fromEntity(foundedAppointment));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = AppointmentCreationDto.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created appointment",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = AppointmentDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad credentials",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User/Interval not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Interval is already booked",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Appointment", operation = "Create new appointment")
    @PostMapping
    public ResponseEntity<?> createAppointment(@Valid @RequestBody AppointmentCreationDto appointmentDto) {
        Appointment createdAppointment = this.appointmentService
                .createAppointment(this.appointmentMapper.fromCreationDto(appointmentDto));
        return ResponseEntity.created(URI.create("/service-api/v1/appointments/appointment/" + createdAppointment.getId()))
                .body(this.appointmentMapper.fromEntity(createdAppointment));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = AppointmentUpdateDto.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated appointment by id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = AppointmentDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad credentials",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User/Interval not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Interval is already booked",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Appointment", operation = "Update appointment by id")
    @PatchMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> updateAppointment(@PathVariable("appointmentId") long appointmentId,
                                               @Valid @RequestBody AppointmentUpdateDto appointmentDto) {
        Appointment updatedAppointment = this.appointmentService
                .updateAppointment(appointmentId, this.appointmentMapper.fromUpdateDto(appointmentDto));
        return ResponseEntity.ok()
                .body(this.appointmentMapper.fromEntity(updatedAppointment));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted appointment"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Appointment not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Appointment", operation = "Delete appointment")
    @DeleteMapping("/appointment/{appointmentId:\\d+}")
    public ResponseEntity<?> deleteAppointmentById(@PathVariable("appointmentId") long appointmentId) {
        this.appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.ok()
                .build();
    }

}
