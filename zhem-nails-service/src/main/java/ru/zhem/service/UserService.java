package ru.zhem.service;

import ru.zhem.entity.Appointment;
import ru.zhem.entity.User;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserService {

    Iterable<User> findUsers();

    Optional<User> findUser(BigDecimal phone);

    User createUser(BigDecimal phone, String name, String surname);

    void updateUser(BigDecimal phone, BigDecimal newPhone, String name, String surname);

    void deleteUser(BigDecimal phone);

    Iterable<Appointment> findAppointmentsByUserPhone(BigDecimal phone);

}
