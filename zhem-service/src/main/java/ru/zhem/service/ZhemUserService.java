package ru.zhem.service;

import ru.zhem.entity.ZhemUser;

import java.util.List;

public interface ZhemUserService {

    List<ZhemUser> findAllUsers(String role);

    ZhemUser findUserById(long userId);

    ZhemUser findUserByPhone(String phone);

    ZhemUser findUserByEmail(String email);

    ZhemUser createUser(ZhemUser user);

    ZhemUser updateUser(long userId, ZhemUser user);

    void deleteUserById(long userId);

}
