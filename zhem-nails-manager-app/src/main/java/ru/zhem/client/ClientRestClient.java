package ru.zhem.client;

import ru.zhem.entity.Client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ClientRestClient {
    List<Client> findAllClients();

    Optional<Client> findClient(BigDecimal phone);

    Client createClient(BigDecimal phone, String name, String surname, String password);

    void updateClient(BigDecimal phone, BigDecimal newPhone, String name, String surname, String password);

    void deleteClient(BigDecimal phone);
}
