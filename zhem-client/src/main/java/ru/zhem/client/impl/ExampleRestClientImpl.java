package ru.zhem.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.zhem.client.interfaces.ExampleRestClient;
import ru.zhem.client.response.PaginatedResponse;
import ru.zhem.dto.request.ExampleDto;
import ru.zhem.dto.response.ExampleCreationDto;
import ru.zhem.exceptions.BadRequestException;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.exceptions.NotFoundException;

import java.util.Map;

@RequiredArgsConstructor
public class ExampleRestClientImpl implements ExampleRestClient {

    private static final ParameterizedTypeReference<PaginatedResponse<ExampleDto>> EXAMPLE_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    @Override
    public Page<ExampleDto> findAllExamples(int size, int page) {
        return this.restClient.get()
                .uri("/service-api/v1/examples?size={size}&page={page}",
                        size, page)
                .retrieve()
                .body(EXAMPLE_TYPE_REFERENCE);
    }

    @Override
    public void createExample(ExampleCreationDto exampleDto) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", exampleDto.getTitle());
        body.add("image", exampleDto.getImage().getResource());
        try {
            this.restClient.post()
                    .uri("/service-api/v1/examples")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            if (problemDetail != null && problemDetail.getProperties() != null
                    && problemDetail.getProperties().containsKey("errors")) {
                throw new CustomBindException((Map<String, String>) problemDetail.getProperties().get("errors"));
            }
            throw new BadRequestException(problemDetail);
        }
    }

    @Override
    public void deleteById(long exampleId) {
        try {
            this.restClient.delete()
                    .uri("/service-api/v1/examples/example/{exampleId}", exampleId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException(problemDetail);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException(exception.getResponseBodyAs(ProblemDetail.class));
        }
    }
}
