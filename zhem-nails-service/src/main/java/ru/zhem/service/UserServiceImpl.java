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
    public Optional<User> findUser(BigDecimal phone) {
        return this.userRepository.findById(phone);
    }

    @Override
    @Transactional
    public User createUser(BigDecimal phone, String name, String surname) {
        return this.userRepository.save(
                new User(phone, name, (surname == null || surname.isBlank()) ? null : surname, null)
        );
    }

    @Override
    @Transactional
    public void updateUser(BigDecimal phone, BigDecimal newPhone, String name, String surname) {
        this.userRepository.findById(phone).ifPresentOrElse((user) -> {
                    if (newPhone == null) {
                        if (name != null && !name.isBlank()) {
                            user.setName(name);
                        }
                        if (surname != null && !surname.isBlank()) {
                            user.setSurname(surname);
                        }
                    } else {
                        User newUser = new User();
                        newUser.setPhone(newPhone);
                        if (name != null && !name.isBlank()) {
                            newUser.setName(name);
                        } else {
                            newUser.setName(user.getName());
                        }
                        if (surname != null && !surname.isBlank()) {
                            newUser.setSurname(surname);
                        } else {
                            newUser.setSurname(user.getSurname());
                        }
                        this.userRepository.delete(user);
                        this.userRepository.save(newUser);
                    }
                },
                () -> {
                    throw new NoSuchElementException("User in not found");
                });
    }

    @Override
    @Transactional
    public void deleteUser(BigDecimal phone) {
        this.userRepository.deleteById(phone);
    }

    @Override
    @Transactional
    public Iterable<Appointment> findAppointmentsByUserPhone(BigDecimal phone) {
        return this.appointmentRepository.findAllByUserPhone(phone);
    }
}
