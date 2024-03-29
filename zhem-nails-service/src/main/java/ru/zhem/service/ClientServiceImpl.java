package ru.zhem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.zhem.entity.Appointment;
import ru.zhem.entity.Client;
import ru.zhem.repository.AppointmentRepository;
import ru.zhem.repository.ClientRepository;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final AppointmentRepository appointmentRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Iterable<Client> findClients() {
        return this.clientRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Client> findClient(BigDecimal phone) {
        return this.clientRepository.findById(phone);
    }

    @Override
    @Transactional
    public Client createClient(BigDecimal phone, String name, String surname, String password) {
        String bcryptPassword = new BCryptPasswordEncoder().encode(password);
        return this.clientRepository.save(
                new Client(phone, name, (surname == null || surname.isBlank()) ? null : surname,
                        bcryptPassword, null)
        );
    }

    @Override
    @Transactional
    public void updateClient(BigDecimal phone, BigDecimal newPhone, String name, String surname, String password) {
        this.clientRepository.findById(phone).ifPresentOrElse((user) -> {
                    if (newPhone == null) {
                        if (name != null && !name.isBlank()) {
                            user.setName(name);
                        }
                        if (surname != null && !surname.isBlank()) {
                            user.setSurname(surname);
                        }
                        if (password != null && !password.isBlank()) {
                            String bcryptPassword = this.passwordEncoder.encode(password);
                            user.setPassword(bcryptPassword);
                        }
                    } else {
                        Client newClient = new Client();
                        newClient.setPhone(newPhone);
                        if (name != null && !name.isBlank()) {
                            newClient.setName(name);
                        } else {
                            newClient.setName(user.getName());
                        }
                        if (surname != null && !surname.isBlank()) {
                            newClient.setSurname(surname);
                        } else {
                            newClient.setSurname(user.getSurname());
                        }
                        if (password != null && !password.isBlank()) {
                            String bcryptPassword = "{bcrypt}" + this.passwordEncoder.encode(password);
                            newClient.setPassword(bcryptPassword);
                        } else {
                            newClient.setPassword(user.getPassword());
                        }
                        this.clientRepository.delete(user);
                        this.clientRepository.save(newClient);
                    }
                },
                () -> {
                    throw new NoSuchElementException("Client in not found");
                });
    }

    @Override
    @Transactional
    public void deleteClient(BigDecimal phone) {
        this.clientRepository.deleteById(phone);
    }

    @Override
    @Transactional
    public Iterable<Appointment> findAppointmentsByClientPhone(BigDecimal phone) {
        return this.appointmentRepository.findAllByClientPhone(phone);
    }
}
