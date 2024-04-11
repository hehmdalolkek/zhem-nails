package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zhem.dto.response.DailyIntervalsDto;
import ru.zhem.dto.request.IntervalCreationDto;
import ru.zhem.dto.request.IntervalUpdateDto;
import ru.zhem.dto.mapper.IntervalMapper;
import ru.zhem.entity.Interval;
import ru.zhem.exception.*;
import ru.zhem.service.IntervalService;

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

    @GetMapping
    public ResponseEntity<?> findAllIntervals(@RequestParam("year") Integer year,
                                              @RequestParam("month") Integer month) {
        try {
            Map<LocalDate, List<Interval>> allIntervals = this.intervalService.findAllIntervalsByYearAndMonth(year, month);
            List<DailyIntervalsDto> intervalsDtos = allIntervals.entrySet().stream()
                    .map(this.intervalMapper::fromEntryOfEntity)
                    .toList();
            return ResponseEntity.ok()
                    .body(intervalsDtos);
        } catch (InvalidDateException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    @GetMapping("/available")
    public ResponseEntity<?> findAllAvailableIntervals(@RequestParam("year") Integer year,
                                                       @RequestParam("month") Integer month) {
        try {
            Map<LocalDate, List<Interval>> allIntervals = this.intervalService.findAllAvailableIntervalsByYearAndMonth(year, month);
            List<DailyIntervalsDto> intervalsDtos = allIntervals.entrySet().stream()
                    .map(this.intervalMapper::fromEntryOfEntity)
                    .toList();
            return ResponseEntity.ok()
                    .body(intervalsDtos);
        } catch (InvalidDateException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    @GetMapping("/interval/{intervalId:\\d+}")
    public ResponseEntity<?> findIntervalById(@PathVariable("intervalId") long intervalId) {
        try {
            Interval foundedInterval = this.intervalService.findIntervalById(intervalId);
            return ResponseEntity.ok()
                    .body(this.intervalMapper.fromEntity(foundedInterval));
        } catch (IntervalNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createInterval(@Valid @RequestBody IntervalCreationDto intervalDto,
                                            BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                Interval createdInterval = this.intervalService
                        .createInterval(this.intervalMapper.fromCreationDto(intervalDto));
                return ResponseEntity
                        .created(URI.create("/service-api/v1/intervals/interval/" + createdInterval.getId()))
                        .body(this.intervalMapper.fromEntity(createdInterval));
            } catch (IntervalWithDuplicateDateTimeException exception) {
                throw new BadRequestException(exception.getMessage());
            }
        }
    }

    @PatchMapping("/interval/{intervalId:\\d+}")
    public ResponseEntity<?> updateInterval(@PathVariable("intervalId") long intervalId,
                                            @Valid @RequestBody IntervalUpdateDto intervalDto,
                                            BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                Interval updatedInterval = this.intervalService
                        .updateInterval(intervalId, this.intervalMapper.fromUpdateDto(intervalDto));
                return ResponseEntity.ok()
                        .body(this.intervalMapper.fromEntity(updatedInterval));
            } catch (IntervalWithDuplicateDateTimeException | NotFoundException exception) {
                throw new BadRequestException(exception.getMessage());
            }
        }
    }

    @DeleteMapping("/interval/{intervalId:\\d+}")
    public ResponseEntity<?> deleteInterval(@PathVariable("intervalId") long intervalId) {
        try {
            this.intervalService.deleteIntervalById(intervalId);
            return ResponseEntity.ok()
                    .build();
        } catch (IntervalNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        } catch (IntervalIsBookedException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

}
