package ru.zhem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.entity.WorkInterval;
import ru.zhem.repository.WorkIntervalRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WorkIntervalServiceImpl implements WorkIntervalService {

    private final WorkIntervalRepository workIntervalRepository;

    @Override
    @Transactional
    public Iterable<WorkInterval> findWorkIntervals() {
        return this.workIntervalRepository.findAllByOrderByDateDescStartTimeAsc();
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
                    throw new NoSuchElementException();
                });
    }

    @Override
    public void deleteWorkInterval(long workIntervalId) {
        this.workIntervalRepository.deleteById(workIntervalId);
    }
}
