package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zhem.common.annotation.LogAnnotation;
import ru.zhem.dto.mapper.IntervalMapper;
import ru.zhem.dto.request.IntervalCreationDto;
import ru.zhem.dto.request.IntervalUpdateDto;
import ru.zhem.dto.response.DailyIntervalsDto;
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

    @LogAnnotation(module = "Interval", operation = "Get interval by id")
    @GetMapping("/interval/{intervalId:\\d+}")
    public ResponseEntity<?> findIntervalById(@PathVariable("intervalId") long intervalId) {
        Interval foundedInterval = this.intervalService.findIntervalById(intervalId);
        return ResponseEntity.ok()
                .body(this.intervalMapper.fromEntity(foundedInterval));
    }

    @LogAnnotation(module = "Interval", operation = "Create new interval")
    @PostMapping
    public ResponseEntity<?> createInterval(@Valid @RequestBody IntervalCreationDto intervalDto) {
        Interval createdInterval = this.intervalService
                .createInterval(this.intervalMapper.fromCreationDto(intervalDto));
        return ResponseEntity
                .created(URI.create("/service-api/v1/intervals/interval/" + createdInterval.getId()))
                .body(this.intervalMapper.fromEntity(createdInterval));
    }

    @LogAnnotation(module = "Interval", operation = "Update interval by id")
    @PatchMapping("/interval/{intervalId:\\d+}")
    public ResponseEntity<?> updateInterval(@PathVariable("intervalId") long intervalId,
                                            @Valid @RequestBody IntervalUpdateDto intervalDto) {
        Interval updatedInterval = this.intervalService
                .updateInterval(intervalId, this.intervalMapper.fromUpdateDto(intervalDto));
        return ResponseEntity.ok()
                .body(this.intervalMapper.fromEntity(updatedInterval));
    }

    @LogAnnotation(module = "Interval", operation = "Delete interval by id")
    @DeleteMapping("/interval/{intervalId:\\d+}")
    public ResponseEntity<?> deleteInterval(@PathVariable("intervalId") long intervalId) {
        this.intervalService.deleteIntervalById(intervalId);
        return ResponseEntity.ok()
                .build();
    }

}
