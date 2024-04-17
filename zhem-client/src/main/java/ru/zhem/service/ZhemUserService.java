package ru.zhem.service;

import ru.zhem.client.response.PaginatedResponse;
import ru.zhem.dto.request.ZhemUserAuthDto;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.dto.response.ZhemUserUpdateDto;
import ru.zhem.entity.ZhemUser;

import java.util.List;

public interface ZhemUserService {

    List<ZhemUserDto> findAllClients();

    PaginatedResponse<ZhemUserDto> findAllClientsByPage(int page, int size);

    ZhemUserDto findUserById(Long id);

    ZhemUserAuthDto findUserAuthByPhone(String phone, boolean isAdmin);

    void createUser(ZhemUser user, boolean isAdmin);

    void updateClient(long userId, ZhemUserUpdateDto user);

    void deleteClient(long userId);

    List<ZhemUserDto> findAllClientsBy(String firstName, String lastName, String phone, String email);

    boolean adminIsExists();

}
