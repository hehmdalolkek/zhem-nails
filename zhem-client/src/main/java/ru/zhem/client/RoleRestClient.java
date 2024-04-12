package ru.zhem.client;

import ru.zhem.dto.request.RoleDto;

import java.util.Optional;

public interface RoleRestClient {

    Optional<RoleDto> findRoleByTitle(String title);

}
