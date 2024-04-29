package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/interval/{intervalId:\\d+}")
    public ResponseEntity<?> findIntervalById(@PathVariable("intervalId") long intervalId) {
        Interval foundedInterval = this.intervalService.findIntervalById(intervalId);
        return ResponseEntity.ok()
                .body(this.intervalMapper.fromEntity(foundedInterval));
    }

    @PostMapping
    public ResponseEntity<?> createInterval(@Valid @RequestBody IntervalCreationDto intervalDto) {
        Interval createdInterval = this.intervalService
                .createInterval(this.intervalMapper.fromCreationDto(intervalDto));
        return ResponseEntity
                .created(URI.create("/service-api/v1/intervals/interval/" + createdInterval.getId()))
                .body(this.intervalMapper.fromEntity(createdInterval));
    }

    @PatchMapping("/interval/{intervalId:\\d+}")
    public ResponseEntity<?> updateInterval(@PathVariable("intervalId") long intervalId,
                                            @Valid @RequestBody IntervalUpdateDto intervalDto) {
        Interval updatedInterval = this.intervalService
                .updateInterval(intervalId, this.intervalMapper.fromUpdateDto(intervalDto));
        return ResponseEntity.ok()
                .body(this.intervalMapper.fromEntity(updatedInterval));
    }

    @DeleteMapping("/interval/{intervalId:\\d+}")
    public ResponseEntity<?> deleteInterval(@PathVariable("intervalId") long intervalId) {
        this.intervalService.deleteIntervalById(intervalId);
        return ResponseEntity.ok()
                .build();
    }

}
