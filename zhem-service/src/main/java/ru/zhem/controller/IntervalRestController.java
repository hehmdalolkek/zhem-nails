package ru.zhem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zhem.common.annotation.LogAnnotation;
import ru.zhem.dto.mapper.IntervalMapper;
import ru.zhem.dto.request.IntervalCreationDto;
import ru.zhem.dto.request.IntervalUpdateDto;
import ru.zhem.dto.response.DailyIntervalsDto;
import ru.zhem.dto.response.IntervalDto;
import ru.zhem.entity.Interval;
import ru.zhem.service.interfaces.IntervalService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/intervals")
public class IntervalRestController {

    private final IntervalService intervalService;

    private final IntervalMapper intervalMapper;

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find all intervals",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = DailyIntervalsDto.class
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
    @LogAnnotation(module = "Interval", operation = "Get all intervals")
    @GetMapping
    public ResponseEntity<?> findAllIntervals(@RequestParam("year") Integer year,
                                              @RequestParam("month") Integer month) {
        Map<LocalDate, List<Interval>> allIntervals = this.intervalService.findAllIntervalsByYearAndMonth(year, month);
        List<DailyIntervalsDto> intervalsDtos = allIntervals.entrySet().stream()
                .map(this.intervalMapper::fromEntryOfEntity)
                .toList();
        return ResponseEntity.ok()
                .body(intervalsDtos);
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find all available intervals",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = DailyIntervalsDto.class
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
    @LogAnnotation(module = "Interval", operation = "Get all available intervals")
    @GetMapping("/available")
    public ResponseEntity<?> findAllAvailableIntervals(@RequestParam("year") Integer year,
                                                       @RequestParam("month") Integer month) {
        Map<LocalDate, List<Interval>> allIntervals = this.intervalService.findAllAvailableIntervalsByYearAndMonth(year, month);
        List<DailyIntervalsDto> intervalsDtos = allIntervals.entrySet().stream()
                .map(this.intervalMapper::fromEntryOfEntity)
                .toList();
        return ResponseEntity.ok()
                .body(intervalsDtos);
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find interval by id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Interval.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Interval not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Interval", operation = "Get interval by id")
    @GetMapping("/interval/{intervalId:\\d+}")
    public ResponseEntity<?> findIntervalById(@PathVariable("intervalId") long intervalId) {
        Interval foundedInterval = this.intervalService.findIntervalById(intervalId);
        return ResponseEntity.ok()
                .body(this.intervalMapper.fromEntity(foundedInterval));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = IntervalCreationDto.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created interval",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = IntervalDto.class
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
                            responseCode = "409",
                            description = "Interval is already exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Interval", operation = "Create new interval")
    @PostMapping
    public ResponseEntity<?> createInterval(@Valid @RequestBody IntervalCreationDto intervalDto) {
        Interval createdInterval = this.intervalService
                .createInterval(this.intervalMapper.fromCreationDto(intervalDto));
        return ResponseEntity
                .created(URI.create("/service-api/v1/intervals/interval/" + createdInterval.getId()))
                .body(this.intervalMapper.fromEntity(createdInterval));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = IntervalCreationDto.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated interval by id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = IntervalDto.class
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
                            responseCode = "409",
                            description = "Interval is already exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Interval", operation = "Update interval by id")
    @PatchMapping("/interval/{intervalId:\\d+}")
    public ResponseEntity<?> updateInterval(@PathVariable("intervalId") long intervalId,
                                            @Valid @RequestBody IntervalUpdateDto intervalDto) {
        Interval updatedInterval = this.intervalService
                .updateInterval(intervalId, this.intervalMapper.fromUpdateDto(intervalDto));
        return ResponseEntity.ok()
                .body(this.intervalMapper.fromEntity(updatedInterval));
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
                            description = "Interval not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Interval", operation = "Delete interval by id")
    @DeleteMapping("/interval/{intervalId:\\d+}")
    public ResponseEntity<?> deleteInterval(@PathVariable("intervalId") long intervalId) {
        this.intervalService.deleteIntervalById(intervalId);
        return ResponseEntity.ok()
                .build();
    }

}
