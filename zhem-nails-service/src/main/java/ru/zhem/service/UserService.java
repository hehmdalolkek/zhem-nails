package ru.zhem.service;

import ru.zhem.entity.Appointment;
import ru.zhem.entity.User;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserService {

    Iterable<User> findUsers();

    Optional<User> findUser(long userId);

    User createUser(BigDecimal phone, String name, String surname);

    void updateUser(long userId, BigDecimal phone, String name, String surname);

    void deleteUser(long userId);

    Iterable<Appointment> findAppointmentsByUserId(long userId);

}
