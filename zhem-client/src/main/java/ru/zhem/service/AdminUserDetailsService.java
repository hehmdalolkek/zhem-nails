package ru.zhem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.zhem.client.ZhemUserRestClient;
import ru.zhem.dto.request.ZhemUserAuthDto;
import ru.zhem.entity.Role;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final ZhemUserRestClient zhemUserRestClient;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        ZhemUserAuthDto user = zhemUserRestClient.findUserAuthByPhone(phone, true)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return User.builder()
                .username(user.getPhone())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(Role::getTitle)
                        .collect(Collectors.joining()))
                .build();
    }
}
