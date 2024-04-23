package ru.zhem.service.interfaces;

import ru.zhem.entity.Role;

public interface RoleService {

    Role createRole(Role role);

    Role findRoleByTitle(String title);

}
