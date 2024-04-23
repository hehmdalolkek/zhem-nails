package ru.zhem.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.zhem.client.interfaces.ZhemServiceRestClient;
import ru.zhem.dto.request.ZhemServiceDto;
import ru.zhem.dto.response.ZhemServiceCreationDto;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ZhemServiceRestClientImpl implements ZhemServiceRestClient {

    private final RestClient restClient;

    private static final ParameterizedTypeReference<List<ZhemServiceDto>> SERVICE_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    @Override
    public List<ZhemServiceDto> findAllServices() {
        return this.restClient.get()
                .uri("/service-api/v1/services")
                .retrieve()
                .body(SERVICE_TYPE_REFERENCE);
    }

    @Override
    public ZhemServiceDto createService(ZhemServiceCreationDto service) {
        try {
            return this.restClient.post()
                    .uri("/service-api/v1/services")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(service)
                    .retrieve()
                    .body(ZhemServiceDto.class);
        } catch (HttpClientErrorException.BadRequest | HttpClientErrorException.Conflict exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new CustomBindException((Map<String, String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteServiceById(int serviceId) {
        try {
            this.restClient.delete()
                    .uri("/service-api/v1/services/service/{serviceId}", serviceId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException(exception.getResponseBodyAs(ProblemDetail.class));
        }
    }
}
