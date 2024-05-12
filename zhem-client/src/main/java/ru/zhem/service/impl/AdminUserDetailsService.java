package ru.zhem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.zhem.client.interfaces.ZhemUserRestClient;
import ru.zhem.dto.request.ZhemUserAuthDto;

import java.util.List;

@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final ZhemUserRestClient zhemUserRestClient;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        ZhemUserAuthDto user = zhemUserRestClient.findUserAuthByPhone(phone
                                .replaceAll("\\D+", "")
                                .replaceFirst("^8", "7"),
                        true)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        List<SimpleGrantedAuthority> roles = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getTitle()))
                .toList();
        return User.builder()
                .username(user.getPhone())
                .password(user.getPassword())
                .authorities(roles)
                .build();
    }
}
