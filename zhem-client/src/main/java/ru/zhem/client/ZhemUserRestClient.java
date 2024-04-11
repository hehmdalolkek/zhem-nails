package ru.zhem.client;

import ru.zhem.client.response.PaginatedResponse;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.dto.response.ZhemUserCreationDto;
import ru.zhem.dto.response.ZhemUserUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ZhemUserRestClient {
    List<ZhemUserDto> findAllUsers(String role);

    PaginatedResponse<ZhemUserDto> findAllUsersByPage(int page, int size);

    Optional<ZhemUserDto> findUserById(Long id, Boolean auth);

    void createUser(ZhemUserCreationDto user);

    void updateClient(long userId, ZhemUserUpdateDto user);

    void deleteClient(long userId);
}
