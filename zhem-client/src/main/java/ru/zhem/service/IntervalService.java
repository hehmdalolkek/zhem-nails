package ru.zhem.service;

import ru.zhem.dto.request.DailyIntervalsDto;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.dto.response.IntervalCreationDto;
import ru.zhem.dto.response.IntervalUpdateDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface IntervalService {

    List<DailyIntervalsDto> findAllIntervals(Integer year, Integer month);

    List<DailyIntervalsDto> findAllAvailableIntervals(Integer year, Integer month);

    IntervalDto findIntervalById(long intervalId);

    void createInterval(IntervalCreationDto interval);

    void updateInterval(long intervalId, IntervalUpdateDto interval);

    void deleteIntervalById(long intervalId);

    Map<LocalDate, List<IntervalDto>> generateIntervalCalendarForMonth(YearMonth yearMonth);


}
