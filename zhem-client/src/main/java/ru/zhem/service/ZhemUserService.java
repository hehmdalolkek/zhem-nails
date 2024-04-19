package ru.zhem.service;

import org.springframework.data.domain.Page;
import ru.zhem.dto.request.ZhemUserAuthDto;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.entity.ZhemUser;

import java.util.List;

public interface ZhemUserService {

    List<ZhemUserDto> findAllClients();

    Page<ZhemUserDto> findAllClientsByPage(int page, int size);

    ZhemUserDto findUserById(Long id);

    ZhemUserAuthDto findUserAuthByPhone(String phone, boolean isAdmin);

    void createUser(ZhemUser user, boolean isAdmin);

    void updateUser(long userId, ZhemUser user);

    void deleteClient(long userId);

    List<ZhemUserDto> findAllClientsBy(String firstName, String lastName, String phone, String email);

    boolean adminIsExists();

    ZhemUserDto findUserByPhone(String phone);
}
