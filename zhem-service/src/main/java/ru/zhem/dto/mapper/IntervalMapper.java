package ru.zhem.dto.mapper;

import org.springframework.stereotype.Component;
import ru.zhem.dto.response.DailyIntervalsDto;
import ru.zhem.dto.request.IntervalCreationDto;
import ru.zhem.dto.response.IntervalDto;
import ru.zhem.dto.request.IntervalUpdateDto;
import ru.zhem.entity.Interval;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class IntervalMapper {

    public Interval fromCreationDto(IntervalCreationDto intervalDto) {
        return Interval.builder()
                .date(intervalDto.getDate())
                .time(intervalDto.getTime())
                .build();
    }

    public Interval fromUpdateDto(IntervalUpdateDto intervalDto) {
        return Interval.builder()
                .date(intervalDto.getDate())
                .time(intervalDto.getTime())
                .status(intervalDto.getStatus())
                .build();
    }

    public IntervalDto fromEntity(Interval interval) {
        return IntervalDto.builder()
                .id(interval.getId())
                .date(interval.getDate())
                .time(interval.getTime())
                .status(interval.getStatus())
                .createdAt(interval.getCreatedAt())
                .updatedAt(interval.getUpdatedAt())
                .build();
    }

    public DailyIntervalsDto fromEntryOfEntity(Map.Entry<LocalDate, List<Interval>> intervalsOfDate) {
        List<IntervalDto> intervalDto = intervalsOfDate.getValue().stream()
                .map(this::fromEntity)
                .toList();
        return DailyIntervalsDto.builder()
                .date(intervalsOfDate.getKey())
                .intervals(intervalDto)
                .build();
    }


}
