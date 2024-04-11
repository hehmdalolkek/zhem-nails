package ru.zhem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.zhem.entity.ZhemUser;

import java.util.List;

public interface ZhemUserService {

    List<ZhemUser> findAllUsers(String role);

    Page<ZhemUser> findAllUsersByPage(Pageable pageable);

    ZhemUser findUserById(long userId);

    ZhemUser findUserByPhone(String phone);

    ZhemUser findUserByEmail(String email);

    ZhemUser createUser(ZhemUser user);

    ZhemUser updateUser(long userId, ZhemUser user);

    void deleteUserById(long userId);

}
