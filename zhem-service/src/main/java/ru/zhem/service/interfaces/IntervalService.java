package ru.zhem.service.interfaces;

import ru.zhem.entity.Interval;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IntervalService {

    Map<LocalDate, List<Interval>> findAllIntervalsByYearAndMonth(Integer year, Integer month);

    Map<LocalDate, List<Interval>> findAllAvailableIntervalsByYearAndMonth(Integer year, Integer month);

    Interval findIntervalById(long intervalId);

    Interval createInterval(Interval interval);

    Interval updateInterval(long intervalId, Interval interval);

    void deleteIntervalById(long intervalId);

}
