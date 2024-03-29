package ru.zhem.client;

import ru.zhem.entity.WorkInterval;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WorkIntervalRestClient {

    Map<LocalDate, List<WorkInterval>> findAllWorkIntervals();

    Map<LocalDate, List<WorkInterval>> findAvailableWorkIntervalsGroupedByDate();

    Optional<WorkInterval> findWorkInterval(long workIntervalId);

    WorkInterval createWorkInterval(LocalDate date, LocalTime time);

    void updateWorkInterval(long workIntervalId, LocalDate date, LocalTime time);

    void deleteWorkInterval(long workIntervalId);

}
