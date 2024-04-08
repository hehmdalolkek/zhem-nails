package ru.zhem.service;

import ru.zhem.entity.Role;
import ru.zhem.entity.ZhemUser;

import java.util.List;
import java.util.Set;

public interface ZhemUserService {

    List<ZhemUser> findAllUsers(String role);

    ZhemUser findUserById(long userId);

    ZhemUser findUserByPhone(String phone);

    ZhemUser findUserByEmail(String email);

    ZhemUser createUser(String phone, String password, String email,
                        String firstName, String lastName, Set<Role> roles);

    ZhemUser updateUser(Long userId, String phone, String password, String email,
                        String firstName, String lastName);

    void deleteUserById(long userId);

}
