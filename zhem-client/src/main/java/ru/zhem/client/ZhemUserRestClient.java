package ru.zhem.client;

import ru.zhem.client.response.PaginatedResponse;
import ru.zhem.dto.request.ZhemUserAuthDto;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.dto.response.ZhemUserCreationDto;
import ru.zhem.dto.response.ZhemUserUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ZhemUserRestClient {
    List<ZhemUserDto> findAllClients();

    PaginatedResponse<ZhemUserDto> findAllClientsByPage(int page, int size);

    Optional<ZhemUserDto> findUserById(Long id);

    Optional<ZhemUserAuthDto> findUserAuthByPhone(String phone, boolean isAdmin);

    void createUser(ZhemUserCreationDto user);

    void updateClient(long userId, ZhemUserUpdateDto user);

    void deleteClient(long userId);

    List<ZhemUserDto> findAllClientsBy(String firstName, String lastName, String phone, String email);

    Boolean adminIsExists();
}
