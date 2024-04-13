package ru.zhem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.client.IntervalRestClient;
import ru.zhem.dto.request.DailyIntervalsDto;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.dto.response.IntervalCreationDto;
import ru.zhem.dto.response.IntervalUpdateDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
        return null;
    }

    @Override
    public IntervalDto findIntervalById(long intervalId) {
        return null;
    }

    @Override
    public void createInterval(IntervalCreationDto interval) {
        this.intervalRestClient.createInterval(interval);
    }

    @Override
    public void updateInterval(long intervalId, IntervalUpdateDto interval) {

    }

    @Override
    public void deleteIntervalById(long intervalId) {

    }

    public Map<LocalDate, List<IntervalDto>> generateIntervalCalendarForMonth(YearMonth yearMonth) {
        Map<LocalDate, List<IntervalDto>> mapOfIntervals = new LinkedHashMap<>();
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            mapOfIntervals.put(date, new ArrayList<>());
        }

        List<DailyIntervalsDto> dailyIntervals =
                this.intervalRestClient.findAllIntervals(yearMonth.getYear(), yearMonth.getMonthValue());
        for (DailyIntervalsDto dailyInterval : dailyIntervals) {
            mapOfIntervals.put(dailyInterval.getDate(), dailyInterval.getIntervals());
        }

        return mapOfIntervals;
    }
}
