package ru.zhem.service;

import ru.zhem.entity.WorkInterval;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WorkIntervalService {

    Map<LocalDate, List<WorkInterval>> findWorkIntervalsGroupedByDate();

    Optional<WorkInterval> findWorkInterval(long workIntervalId);

    WorkInterval createWorkInterval(LocalDate date, LocalTime startTime);

    void updateWorkInterval(long workIntervalId, LocalDate date, LocalTime startTime, Boolean isBooked);

    void deleteWorkInterval(long workIntervalId);

    Map<LocalDate, List<WorkInterval>> findAvailableWorkIntervalsGroupedByDate();
}
