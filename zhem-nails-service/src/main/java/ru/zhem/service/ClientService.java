package ru.zhem.service;

import ru.zhem.entity.Appointment;
import ru.zhem.entity.Client;

import java.math.BigDecimal;
import java.util.Optional;

public interface ClientService {

    Iterable<Client> findClients();

    Optional<Client> findClient(BigDecimal phone);

    Client createClient(BigDecimal phone, String name, String surname);

    void updateClient(BigDecimal phone, BigDecimal newPhone, String name, String surname);

    void deleteClient(BigDecimal phone);

    Iterable<Appointment> findAppointmentsByClientPhone(BigDecimal phone);

}
