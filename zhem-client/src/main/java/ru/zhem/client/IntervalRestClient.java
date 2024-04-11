package ru.zhem.client;

import ru.zhem.dto.request.DailyIntervalsDto;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.dto.response.IntervalCreationDto;
import ru.zhem.dto.response.IntervalUpdateDto;

import java.util.List;
import java.util.Optional;

public interface IntervalRestClient {

    List<DailyIntervalsDto> findAllIntervals(Integer year, Integer month);

    List<DailyIntervalsDto> findAllAvailableIntervals(Integer year, Integer month);

    Optional<IntervalDto> findIntervalById(long intervalId);

    void createInterval(IntervalCreationDto interval);

    void updateInterval(long intervalId, IntervalUpdateDto interval);

    void deleteIntervalById(long intervalId);

}
