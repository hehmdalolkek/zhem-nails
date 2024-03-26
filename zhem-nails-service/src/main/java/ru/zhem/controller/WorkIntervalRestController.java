package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.zhem.controller.payload.NewWorkIntervalPayload;
import ru.zhem.controller.payload.UpdateWorkIntervalPayload;
import ru.zhem.entity.WorkInterval;
import ru.zhem.service.WorkIntervalService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/service-api/v1/workintervals")
public class WorkIntervalRestController {

    private final WorkIntervalService workIntervalService;

    @GetMapping
    public Map<LocalDate, List<WorkInterval>> findWorkIntervals() {
        return this.workIntervalService.findWorkIntervalsGroupedByDate();
    }

    @GetMapping("/{workIntervalId:\\d+}")
    public WorkInterval findWorkInterval(@PathVariable("workIntervalId") long workIntervalId) {
        return this.workIntervalService.findWorkInterval(workIntervalId)
                .orElseThrow(() -> new NoSuchElementException("WorkInterval is not found"));
    }

    @PostMapping
    public ResponseEntity<?> createWorkInterval(@Valid @RequestBody NewWorkIntervalPayload payload,
                                                BindingResult bindingResult,
                                                UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            WorkInterval workInterval = this.workIntervalService.createWorkInterval(payload.date(), payload.startTime());
            return ResponseEntity.created(
                    uriComponentsBuilder
                            .replacePath("/api/v1/workintervals/{workIntervalId}")
                            .build(Map.of("workIntervalId", workInterval.getId()))
            ).body(workInterval);
        }
    }

    @PatchMapping("/{workIntervalId:\\d+}")
    public ResponseEntity<?> updateWorkInterval(@PathVariable("workIntervalId") long workIntervalId,
                                                @Valid @RequestBody UpdateWorkIntervalPayload payload,
                                                BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.workIntervalService.updateWorkInterval(
                    workIntervalId, payload.date(), payload.startTime(), payload.isBooked());
            return ResponseEntity.noContent()
                    .build();
        }
    }

    @DeleteMapping("/{workIntervalId:\\d+}")
    public ResponseEntity<Void> deleteWorkInterval(@PathVariable("workIntervalId") long workIntervalId) {
        this.workIntervalService.deleteWorkInterval(workIntervalId);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/available")
    public Map<LocalDate, List<WorkInterval>> findAvailableWorkIntervals() {
        return this.workIntervalService.findAvailableWorkIntervalsGroupedByDate();
    }

}
