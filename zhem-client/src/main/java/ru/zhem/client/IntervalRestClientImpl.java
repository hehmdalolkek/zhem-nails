package ru.zhem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.zhem.dto.request.DailyIntervalsDto;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.dto.response.IntervalCreationDto;
import ru.zhem.dto.response.IntervalUpdateDto;
import ru.zhem.exceptions.BadRequestException;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class IntervalRestClientImpl implements IntervalRestClient {

    private static final ParameterizedTypeReference<List<DailyIntervalsDto>> INTERVAL_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    @Override
    public List<DailyIntervalsDto> findAllIntervals(Integer year, Integer month) {
        try {
            return this.restClient.get()
                    .uri("/service-api/v1/intervals?year={year}&month={month}",
                            Map.of("year", year, "month", month))
                    .retrieve()
                    .body(INTERVAL_TYPE_REFERENCE);
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new BadRequestException(exception.getResponseBodyAs(ProblemDetail.class));
        }
    }

    @Override
    public List<DailyIntervalsDto> findAllAvailableIntervals(Integer year, Integer month) {
        try {
            return this.restClient.get()
                    .uri("/service-api/v1/intervals/available?year={year}&month={month}",
                            Map.of("year", year, "month", month))
                    .retrieve()
                    .body(INTERVAL_TYPE_REFERENCE);
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new BadRequestException(exception.getResponseBodyAs(ProblemDetail.class));
        }
    }

    @Override
    public Optional<IntervalDto> findIntervalById(long intervalId) {
        try {
            return Optional.ofNullable(
                    this.restClient.get()
                            .uri("/service-api/v1/intervals/interval/{intervalId}",intervalId)
                            .retrieve()
                            .body(IntervalDto.class)
            );
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void createInterval(IntervalCreationDto interval) {
        try {
            this.restClient.post()
                    .uri("/service-api/v1/intervals")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(interval)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest | HttpClientErrorException.Conflict exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new CustomBindException((Map<String, String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void updateInterval(long intervalId, IntervalUpdateDto interval) {
        try {
            this.restClient.patch()
                    .uri("/service-api/v1/intervals/interval/{intervalId}", intervalId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(interval)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest | HttpClientErrorException.Conflict exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new CustomBindException((Map<String, String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException(exception.getResponseBodyAs(ProblemDetail.class));
        }
    }

    @Override
    public void deleteIntervalById(long intervalId) {
        try {
            this.restClient.delete()
                    .uri("/service-api/v1/intervals/interval/{intervalId}", intervalId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.Conflict exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new CustomBindException((Map<String, String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException(exception.getResponseBodyAs(ProblemDetail.class));
        }
    }
}
