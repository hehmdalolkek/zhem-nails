package ru.zhem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.zhem.client.ClientRestClient;
import ru.zhem.entity.Client;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClientUserDetailsService implements UserDetailsService {

    private final ClientRestClient clientRestClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRestClient.findClient(new BigDecimal(username))
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
        return User.builder()
                .username(String.valueOf(client.phone()))
                .password(client.password())
                .roles("USER")
                .build();
    }
}
