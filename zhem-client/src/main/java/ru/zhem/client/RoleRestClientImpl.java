package ru.zhem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;
import ru.zhem.dto.request.RoleDto;

import java.util.Optional;
@RequiredArgsConstructor
public class RoleRestClientImpl implements RoleRestClient {

    private final RestClient restClient;

    @Override
    public Optional<RoleDto> findRoleByTitle(String title) {
        return Optional.ofNullable(
                this.restClient.get()
                        .uri("/service-api/v1/roles/role/{title}",title)
                        .retrieve()
                        .body(RoleDto.class)
        );
    }
}
