package ru.zhem.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.entity.Interval;
import ru.zhem.entity.Status;
import ru.zhem.common.exception.IntervalIsBookedException;
import ru.zhem.common.exception.IntervalNotFoundException;
import ru.zhem.common.exception.IntervalWithDuplicateDateTimeException;
import ru.zhem.repository.IntervalRepository;
import ru.zhem.service.interfaces.IntervalService;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

@Service
@RequiredArgsConstructor
public class IntervalServiceImpl implements IntervalService {

    private final IntervalRepository intervalRepository;

    @Override
    @Transactional
    public Map<LocalDate, List<Interval>> findAllIntervalsByYearAndMonth(Integer year, Integer month) {
        YEAR.checkValidValue(year);
        MONTH_OF_YEAR.checkValidValue(month);
        List<Interval> intervals = this.intervalRepository
                .findAllByDateOrderByDateTime(year, month);
        return intervals.stream()
                .collect(Collectors.groupingBy(Interval::getDate,
                        LinkedHashMap::new, Collectors.toList()));
    }

    @Override
    @Transactional
    public Map<LocalDate, List<Interval>> findAllAvailableIntervalsByYearAndMonth(Integer year, Integer month) {
        YEAR.checkValidValue(year);
        MONTH_OF_YEAR.checkValidValue(month);
        List<Interval> intervals = this.intervalRepository
                .findAllByDateAndStatusOrderByDateTime(year, month, Status.AVAILABLE);
        return intervals.stream()
                .collect(Collectors.groupingBy(Interval::getDate,
                        LinkedHashMap::new, Collectors.toList()));
    }

    @Override
    @Transactional
    public Interval findIntervalById(long intervalId) {
        return this.intervalRepository.findById(intervalId)
                .orElseThrow(() -> new IntervalNotFoundException("Interval not found"));
    }

    @Override
    @Transactional
    public Interval createInterval(Interval interval) {
        boolean isExists = this.intervalRepository.existsByDateAndTime(interval.getDate(), interval.getTime());
        if (isExists) {
            throw new IntervalWithDuplicateDateTimeException("Interval with this date and time is already exists");
        }
        interval.setStatus(Status.AVAILABLE);
        return this.intervalRepository.save(interval);
    }

    @Override
    public Interval updateInterval(long intervalId, Interval interval) {
        Interval foundedInterval = this.intervalRepository.findById(intervalId)
                .orElseThrow(() -> new IntervalNotFoundException("Interval not found"));

        if (Objects.nonNull(interval.getDate())) {
            foundedInterval.setDate(interval.getDate());
        }
        if (Objects.nonNull(interval.getTime())) {
            foundedInterval.setTime(interval.getTime());
        }
        if (Objects.nonNull(interval.getStatus())) {
            foundedInterval.setStatus(interval.getStatus());
        }

        boolean isExists = this.intervalRepository.existsByDateAndTimeAndIdNot(
                foundedInterval.getDate(), foundedInterval.getTime(), foundedInterval.getId());
        if (isExists) {
            throw new IntervalWithDuplicateDateTimeException("Interval with this date and time is already exists");
        }

        return this.intervalRepository.save(foundedInterval);
    }

    @Override
    @Transactional
    public void deleteIntervalById(long intervalId) {
        Interval foundedInterval = this.intervalRepository.findById(intervalId)
                .orElseThrow(() -> new IntervalNotFoundException("Interval not found"));
        if (foundedInterval.getStatus() == Status.BOOKED) {
            throw new IntervalIsBookedException("Interval is booked");
        }
        this.intervalRepository.deleteById(intervalId);
    }
}
