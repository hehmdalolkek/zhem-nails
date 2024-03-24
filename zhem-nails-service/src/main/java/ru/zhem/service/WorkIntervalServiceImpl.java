package ru.zhem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.entity.WorkInterval;
import ru.zhem.repository.WorkIntervalRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WorkIntervalServiceImpl implements WorkIntervalService {

    private final WorkIntervalRepository workIntervalRepository;

    @Override
    @Transactional
    public Map<LocalDate, List<WorkInterval>> findWorkIntervalsGroupedByDate() {
        List<WorkInterval> workIntervals = this.workIntervalRepository.findAllByOrderByDateAscStartTime();
        return workIntervals.stream()
                .collect(Collectors.groupingBy(
                                WorkInterval::getDate, LinkedHashMap::new, Collectors.toList()
                        )
                );
    }

    @Override
    @Transactional
    public Optional<WorkInterval> findWorkInterval(long workIntervalId) {
        return this.workIntervalRepository.findById(workIntervalId);
    }

    @Override
    @Transactional
    public WorkInterval createWorkInterval(LocalDate date, LocalTime startTime) {
        return this.workIntervalRepository.save(new WorkInterval(null, date, startTime, false));
    }

    @Override
    @Transactional
    public void updateWorkInterval(long workIntervalId, LocalDate date, LocalTime startTime, Boolean isBooked) {
        this.workIntervalRepository.findById(workIntervalId)
                .ifPresentOrElse((workInterval) -> {
                    if (date != null) {
                        workInterval.setDate(date);
                    }
                    if (startTime != null) {
                        workInterval.setStartTime(startTime);
                    }
                    if (isBooked != null) {
                        workInterval.setIsBooked(isBooked);
                    }
                }, () -> {
                    throw new NoSuchElementException("WorkInterval is not found");
                });
    }

    @Override
    @Transactional
    public void deleteWorkInterval(long workIntervalId) {
        WorkInterval workInterval = workIntervalRepository.findById(workIntervalId)
                .orElseThrow(() -> new NoSuchElementException("WorkInterval is not found"));
        if (workInterval.getIsBooked().equals(true)) {
            throw new IllegalStateException("WorkInterval is booked");
        }
        this.workIntervalRepository.deleteById(workIntervalId);
    }

    @Override
    @Transactional
    public Map<LocalDate, List<WorkInterval>> findAvailableWorkIntervalsGroupedByDate() {
        List<WorkInterval> workIntervals = this.workIntervalRepository
                .findWorkIntervalsByIsBookedIsFalseAndDateGreaterThanOrderByStartTime(LocalDate.now());
        return workIntervals.stream()
                .collect(Collectors.groupingBy(WorkInterval::getDate,
                        LinkedHashMap::new, Collectors.toList()));
    }
}
