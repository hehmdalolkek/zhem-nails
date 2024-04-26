package ru.zhem.service.interfaces;

import ru.zhem.dto.request.DailyIntervalsDto;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.dto.response.IntervalCreationDto;
import ru.zhem.dto.response.IntervalUpdateDto;

import java.util.List;

public interface IntervalService {

    List<DailyIntervalsDto> findAllIntervals(Integer year, Integer month);

    List<DailyIntervalsDto> findAllAvailableIntervals(Integer year, Integer month);

    IntervalDto findIntervalById(long intervalId);

    void createInterval(IntervalCreationDto interval);

    void updateInterval(long intervalId, IntervalUpdateDto interval);

    void deleteIntervalById(long intervalId);


}
