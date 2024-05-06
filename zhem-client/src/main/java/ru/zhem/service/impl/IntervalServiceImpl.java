package ru.zhem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.client.interfaces.IntervalRestClient;
import ru.zhem.common.exceptions.IntervalNotFoundException;
import ru.zhem.dto.request.DailyIntervalsDto;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.dto.response.IntervalCreationDto;
import ru.zhem.dto.response.IntervalUpdateDto;
import ru.zhem.service.interfaces.IntervalService;

import java.time.YearMonth;
import java.util.List;


@RequiredArgsConstructor
@Service
public class IntervalServiceImpl implements IntervalService {

    private final IntervalRestClient intervalRestClient;

    @Override
    public List<DailyIntervalsDto> findAllIntervals(Integer year, Integer month) {
        return this.intervalRestClient.findAllIntervals(year, month);
    }

    @Override
    public List<DailyIntervalsDto> findAllAvailableIntervals(Integer year, Integer month) {
        if (year == null || month == null) {
            year = YearMonth.now().getYear();
            month = YearMonth.now().getMonthValue();
        }
        return this.intervalRestClient.findAllAvailableIntervals(year, month);
    }

    @Override
    public IntervalDto findIntervalById(long intervalId) {
        return this.intervalRestClient.findIntervalById(intervalId)
                .orElseThrow(() -> new IntervalNotFoundException("Interval not found"));
    }

    @Override
    public void createInterval(IntervalCreationDto interval) {
        this.intervalRestClient.createInterval(interval);
    }

    @Override
    public void updateInterval(long intervalId, IntervalUpdateDto interval) {
        this.intervalRestClient.updateInterval(intervalId, interval);
    }

    @Override
    public void deleteIntervalById(long intervalId) {
        this.intervalRestClient.deleteIntervalById(intervalId);
    }
}
