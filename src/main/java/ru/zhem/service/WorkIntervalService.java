package ru.zhem.service;

import ru.zhem.entity.WorkInterval;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface WorkIntervalService {

    Iterable<WorkInterval> findWorkIntervals();

    Optional<WorkInterval> findWorkInterval(long workIntervalId);

    WorkInterval createWorkInterval(LocalDate date, LocalTime startTime);

    void updateWorkInterval(long workIntervalId, LocalDate date, LocalTime startTime, Boolean isBooked);

    void deleteWorkInterval(long workIntervalId);

}
