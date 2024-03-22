package ru.zhem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import ru.zhem.entity.WorkInterval;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RestClientWorkIntervalRestClient implements WorkIntervalRestClient {

    private static final ParameterizedTypeReference<List<WorkInterval>> WORK_INTERVALS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    @Override
    public List<WorkInterval> findAllWorkIntervals() {
        return this.restClient
                .get()
                .uri("/service-api/v1/workintervals")
                .retrieve()
                .body(WORK_INTERVALS_TYPE_REFERENCE);
    }

    @Override
    public List<WorkInterval> findAvailableWorkIntervalsGroupedByDate() {
        return null;
    }

    @Override
    public Optional<WorkInterval> findWorkInterval(long workIntervalId) {
        return Optional.empty();
    }

    @Override
    public WorkInterval createWorkInterval(LocalDate date, LocalTime time) {
        return null;
    }

    @Override
    public void updateWorkInterval(long workIntervalId, LocalDate date, LocalTime time) {

    }

    @Override
    public void deleteWorkInterval(long workIntervalId) {

    }
}
