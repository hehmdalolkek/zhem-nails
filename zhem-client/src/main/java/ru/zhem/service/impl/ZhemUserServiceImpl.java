package ru.zhem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.zhem.client.interfaces.RoleRestClient;
import ru.zhem.client.interfaces.ZhemUserRestClient;
import ru.zhem.dto.request.RoleDto;
import ru.zhem.dto.request.ZhemUserAuthDto;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.dto.response.ZhemUserCreationDto;
import ru.zhem.dto.response.ZhemUserUpdateDto;
import ru.zhem.entity.ZhemUser;
import ru.zhem.exceptions.RoleNotFoundException;
import ru.zhem.service.interfaces.ZhemUserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ZhemUserServiceImpl implements ZhemUserService {

    private final ZhemUserRestClient zhemUserRestClient;

    private final RoleRestClient roleRestClient;

    @Override
    public List<ZhemUserDto> findAllClients() {
        return this.zhemUserRestClient.findAllClients();
    }

    @Override
    public Page<ZhemUserDto> findAllClientsByPage(int page, int size) {
        return this.zhemUserRestClient.findAllClientsByPage(page, size);
    }

    @Override
    public ZhemUserDto findUserById(Long id) {
        return null;
    }

    @Override
    public ZhemUserAuthDto findUserAuthByPhone(String phone, boolean isAdmin) {
        return null;
    }

    @Override
    public void createUser(ZhemUser user, boolean isAdmin) {
        Set<RoleDto> roles = new HashSet<>();
        roles.add(this.roleRestClient.findRoleByTitle("CLIENT")
                .orElseThrow(() -> new RoleNotFoundException("Role not found")));
        if (isAdmin) {
            roles.add(this.roleRestClient.findRoleByTitle("ADMIN")
                    .orElseThrow(() -> new RoleNotFoundException("Role not found")));
        }
        ZhemUserCreationDto userDto = ZhemUserCreationDto.builder()
                .phone(user.getPhone())
                .email(user.getEmail() != null && user.getEmail().isBlank() ? null : user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName() != null && user.getLastName().isBlank()
                        && user.getLastName().length() < 2 ? null
                        : user.getLastName())
                .password(user.getPassword())
                .roles(roles)
                .build();
        this.zhemUserRestClient.createUser(userDto);
    }

    @Override
    public void updateUser(long userId, ZhemUser user) {
        ZhemUserUpdateDto userDto = ZhemUserUpdateDto.builder()
                .phone(user.getPhone())
                .email(user.getEmail() != null && user.getEmail().isBlank() ? null : user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName() != null && user.getLastName().isBlank()
                        && user.getLastName().length() < 2 ? null
                        : user.getLastName())
                .password(user.getPassword())
                .build();
        this.zhemUserRestClient.updateUser(userId, userDto);
    }

    @Override
    public void deleteClient(long userId) {

    }

    @Override
    public List<ZhemUserDto> findAllClientsBy(String firstName, String lastName, String phone, String email) {
        return this.zhemUserRestClient.findAllClientsBy(firstName, lastName, phone, email);
    }

    @Override
    public boolean adminIsExists() {
        return this.zhemUserRestClient.adminIsExists();
    }

    @Override
    public ZhemUserDto findUserByPhone(String phone) {
        return this.zhemUserRestClient.findUserByPhone(phone);
    }

    @Override
    public ZhemUserDto findAdmin() {
        return this.zhemUserRestClient.findAdmin();
    }
}
