package ru.zhem.client.interfaces;

import ru.zhem.dto.request.RoleDto;

import java.util.Optional;

public interface RoleRestClient {

    Optional<RoleDto> findRoleByTitle(String title);

}
