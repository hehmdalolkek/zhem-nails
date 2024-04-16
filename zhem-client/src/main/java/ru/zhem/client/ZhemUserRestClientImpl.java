package ru.zhem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.zhem.client.response.PaginatedResponse;
import ru.zhem.dto.request.ZhemUserAuthDto;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.dto.response.ZhemUserCreationDto;
import ru.zhem.dto.response.ZhemUserUpdateDto;
import ru.zhem.exceptions.BadRequestException;
import ru.zhem.exceptions.CustomBindException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ZhemUserRestClientImpl implements ZhemUserRestClient {

    private static final ParameterizedTypeReference<List<ZhemUserDto>> USER_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private static final ParameterizedTypeReference<PaginatedResponse<ZhemUserDto>> USER_PAGE_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    @Override
    public List<ZhemUserDto> findAllUsers(String role) {
        try {
            return restClient.get()
                    .uri("/service-api/v1/users?role={role}", role)
                    .retrieve()
                    .body(USER_TYPE_REFERENCE);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException(problemDetail);
        }
    }

    @Override
    public PaginatedResponse<ZhemUserDto> findAllUsersByPage(int page, int size) {
        return this.restClient.get()
                .uri("/service-api/v1/users/pageable?page={page}&size={size}",
                        page, size)
                .retrieve()
                .body(USER_PAGE_TYPE_REFERENCE);
    }

    @Override
    public Optional<ZhemUserDto> findUserById(Long id) {
        try {
            return Optional.ofNullable(
                    this.restClient.get()
                            .uri("/service-api/v1/users/user/{userId}", id)
                            .retrieve()
                            .body(ZhemUserDto.class)
            );
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ZhemUserAuthDto> findUserAuthByPhone(String phone, boolean isAdmin) {
        try {
            return Optional.ofNullable(
                    this.restClient.get()
                            .uri("/service-api/v1/users/user/auth/{phone}?admin={isAdmin}", phone, isAdmin)
                            .retrieve()
                            .body(ZhemUserAuthDto.class)
            );
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void createUser(ZhemUserCreationDto user) {
        try {
            this.restClient.post()
                    .uri("/service-api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(user)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new CustomBindException((Map<String, String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void updateClient(long userId, ZhemUserUpdateDto user) {

    }

    @Override
    public void deleteClient(long userId) {

    }

    @Override
    public List<ZhemUserDto> findAllUsersBy(String firstName, String lastName, String phone, String email) {
        return this.restClient.get()
                .uri("/service-api/v1/users?" +
                        "firstName={firstName}&lastName={lastName}&phone={phone}&email={email}",
                        firstName, lastName, phone, email)
                .retrieve()
                .body(USER_TYPE_REFERENCE);
    }
}
