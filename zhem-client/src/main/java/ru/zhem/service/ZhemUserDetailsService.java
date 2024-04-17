package ru.zhem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.zhem.client.RoleRestClient;
import ru.zhem.client.ZhemUserRestClient;
import ru.zhem.client.response.PaginatedResponse;
import ru.zhem.dto.request.RoleDto;
import ru.zhem.dto.request.ZhemUserAuthDto;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.dto.response.ZhemUserCreationDto;
import ru.zhem.dto.response.ZhemUserUpdateDto;
import ru.zhem.entity.ZhemUser;
import ru.zhem.exceptions.RoleNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Qualifier("zhemUserDetailsService")
@Service
@RequiredArgsConstructor
public class ZhemUserDetailsService implements UserDetailsService, ZhemUserService {

    private final ZhemUserRestClient zhemUserRestClient;

    private final RoleRestClient roleRestClient;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        ZhemUserAuthDto user = zhemUserRestClient.findUserAuthByPhone(phone, false)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return User.builder()
                .username(user.getPhone())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(RoleDto::getTitle)
                        .collect(Collectors.joining()))
                .build();
    }

    @Override
    public List<ZhemUserDto> findAllClients() {
        return this.zhemUserRestClient.findAllClients();
    }

    @Override
    public PaginatedResponse<ZhemUserDto> findAllClientsByPage(int page, int size) {
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
                .email(user.getEmail().isBlank() ? null : user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName().isBlank() ? null : user.getLastName())
                .password(user.getPassword())
                .roles(roles)
                .build();
        this.zhemUserRestClient.createUser(userDto);
    }

    @Override
    public void updateClient(long userId, ZhemUserUpdateDto user) {

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
}
