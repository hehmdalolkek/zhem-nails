package ru.zhem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ru.zhem.dto.request.DailyIntervalsDto;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.dto.response.IntervalCreationDto;
import ru.zhem.dto.response.IntervalUpdateDto;

import java.util.*;

@RequiredArgsConstructor
public class IntervalRestClientImpl implements IntervalRestClient {

    private static final ParameterizedTypeReference<List<DailyIntervalsDto>> INTERVAL_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    @Override
    public List<DailyIntervalsDto> findAllIntervals(Integer year, Integer month) {
        return this.restClient.get()
                .uri("/service-api/v1/intervals?year={year}&month={month}",
                        Map.of("year", year, "month", month))
                .retrieve()
                .body(INTERVAL_TYPE_REFERENCE);
    }

    @Override
    public List<DailyIntervalsDto> findAllAvailableIntervals(Integer year, Integer month) {
        return this.restClient.get()
                .uri("/service-api/v1/intervals/available?year={year}&month={month}",
                        Map.of("year", year, "month", month))
                .retrieve()
                .body(INTERVAL_TYPE_REFERENCE);
    }

    @Override
    public Optional<IntervalDto> findIntervalById(long intervalId) {
        return Optional.ofNullable(
                this.restClient.get()
                        .uri("/service-api/v1/intervals/interval/{intervalId}",intervalId)
                        .retrieve()
                        .body(IntervalDto.class)
        );
    }

    @Override
    public void createInterval(IntervalCreationDto interval) {
        this.restClient.post()
                .uri("/service-api/v1/intervals")
                .contentType(MediaType.APPLICATION_JSON)
                .body(interval)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void updateInterval(long intervalId, IntervalUpdateDto interval) {

    }

    @Override
    public void deleteIntervalById(long intervalId) {

    }
}
