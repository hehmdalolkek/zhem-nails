package ru.zhem.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.zhem.client.interfaces.RoleRestClient;
import ru.zhem.dto.request.RoleDto;

import java.util.Optional;
@RequiredArgsConstructor
public class RoleRestClientImpl implements RoleRestClient {

    private final RestClient restClient;

    @Override
    public Optional<RoleDto> findRoleByTitle(String title) {
        try {
            return Optional.ofNullable(
                    this.restClient.get()
                            .uri("/service-api/v1/roles/role/{title}",title)
                            .retrieve()
                            .body(RoleDto.class)
            );
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }
}
