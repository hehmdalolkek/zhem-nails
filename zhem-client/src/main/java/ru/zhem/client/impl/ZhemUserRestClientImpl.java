package ru.zhem.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.zhem.client.interfaces.ZhemUserRestClient;
import ru.zhem.dto.response.PaginatedResponse;
import ru.zhem.dto.request.ZhemUserAuthDto;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.dto.response.ZhemUserCreationDto;
import ru.zhem.dto.response.ZhemUserUpdateDto;
import ru.zhem.common.exceptions.CustomBindException;
import ru.zhem.common.exceptions.NotFoundException;

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
    public List<ZhemUserDto> findAllClients() {
        return restClient.get()
                .uri("/service-api/v1/users")
                .retrieve()
                .body(USER_TYPE_REFERENCE);
    }

    @Override
    public Page<ZhemUserDto> findAllClientsByPage(int page, int size) {
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
        } catch (HttpClientErrorException.BadRequest | HttpClientErrorException.Conflict exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new CustomBindException((Map<String, String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void updateUser(long userId, ZhemUserUpdateDto user) {
        try {
            this.restClient.patch()
                    .uri("/service-api/v1/users/user/{userId}", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(user)
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
    public void deleteClient(long userId) {

    }

    @Override
    public List<ZhemUserDto> findAllClientsBy(String firstName, String lastName, String phone, String email) {
        return this.restClient.get()
                .uri("/service-api/v1/users?" +
                        "firstName={firstName}&lastName={lastName}&phone={phone}&email={email}",
                        firstName, lastName, phone, email)
                .retrieve()
                .body(USER_TYPE_REFERENCE);
    }

    @Override
    public Boolean adminIsExists() {
        return this.restClient.get()
                .uri("/service-api/v1/users/user/check/admin")
                .retrieve()
                .body(Boolean.class);
    }

    @Override
    public Optional<ZhemUserDto> findUserByPhone(String phone) {
        try {
            return Optional.ofNullable(this.restClient.get()
                    .uri("/service-api/v1/users/user/phone/{phone}", phone)
                    .retrieve()
                    .body(ZhemUserDto.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ZhemUserDto> findAdmin() {
        try {
            return Optional.ofNullable(this.restClient.get()
                    .uri("/service-api/v1/users/user/admin")
                    .retrieve()
                    .body(ZhemUserDto.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }
}
