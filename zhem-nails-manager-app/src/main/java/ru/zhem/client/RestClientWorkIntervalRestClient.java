package ru.zhem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.zhem.entity.WorkInterval;
import ru.zhem.entity.payload.NewWorkIntervalPayload;
import ru.zhem.entity.payload.UpdateWorkIntervalPayload;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class RestClientWorkIntervalRestClient implements WorkIntervalRestClient {

    private static final ParameterizedTypeReference<Map<LocalDate, List<WorkInterval>>> WORK_INTERVALS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    @Override
    public Map<LocalDate, List<WorkInterval>> findAllWorkIntervals() {
        return this.restClient.get()
                .uri("/service-api/v1/workintervals")
                .retrieve()
                .body(WORK_INTERVALS_TYPE_REFERENCE);
    }

    @Override
    public Map<LocalDate, List<WorkInterval>> findAvailableWorkIntervalsGroupedByDate() {
        return this.restClient.get()
                .uri("/service-api/v1/workintervals/available")
                .retrieve()
                .body(WORK_INTERVALS_TYPE_REFERENCE);
    }

    @Override
    public Optional<WorkInterval> findWorkInterval(long workIntervalId) {
        try {
            return Optional.ofNullable(
                    this.restClient.get()
                            .uri("/service-api/v1/workintervals/{workIntervalId}", workIntervalId)
                            .retrieve()
                            .body(WorkInterval.class)
            );
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public WorkInterval createWorkInterval(LocalDate date, LocalTime time) {
        try {
            return this.restClient.post()
                    .uri("/service-api/v1/workintervals")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewWorkIntervalPayload(date, time))
                    .retrieve()
                    .body(WorkInterval.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void updateWorkInterval(long workIntervalId, LocalDate date, LocalTime time) {
        try {
            this.restClient.patch()
                    .uri("/service-api/v1/workintervals/{workIntervalId}", workIntervalId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateWorkIntervalPayload(date, time))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NoSuchElementException(exception);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteWorkInterval(long workIntervalId) {
        try {
            this.restClient
                    .delete()
                    .uri("/service-api/v1/workintervals/{workIntervalId}", workIntervalId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NoSuchElementException(exception);
        }
    }
}
