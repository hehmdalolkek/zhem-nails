package ru.zhem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.entity.Appointment;
import ru.zhem.entity.User;
import ru.zhem.repository.AppointmentRepository;
import ru.zhem.repository.UserRepository;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public Iterable<User> findUsers() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<User> findUser(long userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    @Transactional
    public User createUser(BigDecimal phone, String name, String surname) {
        return this.userRepository.save(
                new User(null, phone, name, surname.isBlank() ? null : surname, null)
        );
    }

    @Override
    @Transactional
    public void updateUser(long userId, BigDecimal phone, String name, String surname) {
        this.userRepository.findById(userId)
                .ifPresentOrElse((user) -> {
                            if (phone != null) {
                                user.setPhone(phone);
                            }
                            if (name != null && !name.isBlank()) {
                                user.setName(name);
                            }
                            if (surname != null && !surname.isBlank()) {
                                user.setSurname(surname);
                            }
                        },
                        () -> {
                            throw new NoSuchElementException();
                        });
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        this.userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public Iterable<Appointment> findAppointmentsByUserId(long userId) {
        return this.appointmentRepository.findAllByUserId(userId);
    }
}
