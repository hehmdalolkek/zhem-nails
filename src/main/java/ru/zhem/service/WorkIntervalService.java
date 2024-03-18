package ru.zhem.service;

import ru.zhem.entity.WorkDay;
import ru.zhem.entity.WorkInterval;

import java.time.LocalTime;
import java.util.Optional;

public interface WorkIntervalService {

    Iterable<WorkInterval> findWorkIntervals();

    Iterable<WorkInterval> findWorkIntervalsByWorkDay(WorkDay workDay);

    Iterable<WorkInterval> findWorkNotBookedIntervalsByWorkDay(WorkDay workDay);

    Optional<WorkInterval> findWorkInterval(long workIntervalId);

    WorkInterval createWorkInterval(WorkDay workDay, LocalTime startTime);

    void updateWorkInterval(Long workIntervalId, WorkDay workDay, LocalTime startTime, Boolean isBooked);

    void deleteWorkInterval(long workIntervalId);

}
