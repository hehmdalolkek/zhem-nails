package ru.zhem.service;

import ru.zhem.entity.WorkDay;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkDayService {

    Iterable<WorkDay> findWorkDays();

    Iterable<WorkDay> findWorkDaysByDate(int year, int month);

    Optional<WorkDay> findWorkDay(long workDayId);

    WorkDay createWorkDay(LocalDate date);

    void updateWorkDay(Long workDayId, LocalDate date);

    void deleteWorkDay(long workDayId);
}
