package ru.zhem.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.zhem.client.interfaces.PostRestClient;
import ru.zhem.client.response.PaginatedResponse;
import ru.zhem.dto.request.PostDto;
import ru.zhem.dto.response.PostCreationDto;
import ru.zhem.exceptions.BadRequestException;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.exceptions.NotFoundException;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class PostRestClientImpl implements PostRestClient {

    private static final ParameterizedTypeReference<PaginatedResponse<PostDto>> POST_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;


    @Override
    public Page<PostDto> findAllPosts(int size, int page) {
        return this.restClient.get()
                .uri("/service-api/v1/posts?size={size}&page={page}", size, page)
                .retrieve()
                .body(POST_TYPE_REFERENCE);
    }

    @Override
    public Optional<PostDto> findPostById(long postId) {
        try {
            return Optional.ofNullable(this.restClient.get()
                    .uri("/service-api/v1/posts/post/{postId}", postId)
                    .retrieve()
                    .body(PostDto.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void createPost(PostCreationDto postDto) {
        try {
            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
            parts.add("content", postDto.getContent());
            parts.add("image", postDto.getImage().getResource());
            this.restClient.post()
                    .uri("/service-api/v1/posts")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(parts)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            if (problemDetail != null && problemDetail.getProperties() != null
                    && problemDetail.getProperties().containsKey("errors")) {
                throw new CustomBindException((Map<String, String>) problemDetail.getProperties().get("errors"));
            }
            throw new BadRequestException(problemDetail);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException(exception.getResponseBodyAs(ProblemDetail.class));
        }
    }

    @Override
    public void deleteById(long postId) {
        try {
            this.restClient.delete()
                    .uri("/service-api/v1/posts/post/{postId}", postId)
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
