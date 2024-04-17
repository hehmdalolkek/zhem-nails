package ru.zhem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.zhem.entity.ZhemUser;

import java.util.List;

public interface ZhemUserService {

    List<ZhemUser> findAllClients();

    Page<ZhemUser> findAllClientsByPage(Pageable pageable);

    ZhemUser findUserById(long userId);

    ZhemUser findUserByPhone(String phone, boolean isAdmin);

    ZhemUser findUserByEmail(String email);

    ZhemUser createUser(ZhemUser user);

    ZhemUser updateUser(long userId, ZhemUser user);

    void deleteUserById(long userId);

    List<ZhemUser> findAllClientsBy(String firstName, String lastName, String phone, String email);

    Boolean adminIsExists();
}
