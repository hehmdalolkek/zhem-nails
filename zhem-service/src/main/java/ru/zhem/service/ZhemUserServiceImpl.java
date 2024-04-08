package ru.zhem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.zhem.entity.Role;
import ru.zhem.entity.ZhemUser;
import ru.zhem.exception.RoleNotFoundException;
import ru.zhem.exception.ZhemUserNotFoundException;
import ru.zhem.exception.ZhemUserWithDuplicateEmailException;
import ru.zhem.exception.ZhemUserWithDuplicatePhoneException;
import ru.zhem.repository.RoleRepository;
import ru.zhem.repository.ZhemUserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ZhemUserServiceImpl implements ZhemUserService {

    private final ZhemUserRepository zhemUserRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public List<ZhemUser> findAllUsers(String role) {
        if (Objects.nonNull(role)) {
            Role foundedRole = this.roleRepository.findByTitleIgnoreCase(role)
                    .orElseThrow(() -> new RoleNotFoundException("Role not found"));
            return this.zhemUserRepository.findAllByRolesContains(foundedRole);
        } else {
            return this.zhemUserRepository.findAll();
        }
    }

    @Override
    @Transactional
    public ZhemUser findUserById(long userId) {
        return this.zhemUserRepository.findById(userId)
                .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public ZhemUser findUserByPhone(String phone) {
        return this.zhemUserRepository.findByPhone(phone)
                .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public ZhemUser findUserByEmail(String email) {
        return this.zhemUserRepository.findByEmail(email)
                .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public ZhemUser createUser(String phone, String password, String email,
                               String firstName, String lastName, Set<Role> roles) {
        boolean phoneIsExists = this.zhemUserRepository.existsByPhone(phone);
        boolean emailIsExists = this.zhemUserRepository.existsByEmail(email);
        if (phoneIsExists) {
            throw new ZhemUserWithDuplicatePhoneException("User with this phone is already exists");
        }
        if (emailIsExists) {
            throw new ZhemUserWithDuplicateEmailException("User with this email is already exists");
        }

        return this.zhemUserRepository.save(ZhemUser.builder()
                .phone(phone)
                .password(passwordEncoder.encode(password))
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .roles(roles)
                .build());
    }

    @Override
    @Transactional
    public ZhemUser updateUser(Long userId, String phone, String password, String email,
                               String firstName, String lastName) {
        ZhemUser user = this.zhemUserRepository.findById(userId)
                .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
        if (Objects.nonNull(phone)) {
            boolean phoneIsExists = this.zhemUserRepository.existsByPhone(phone);
            if (phoneIsExists && !user.getPhone().equals(phone)) {
                throw new ZhemUserWithDuplicatePhoneException("User with this phone is already exists");
            } else {
                user.setPhone(phone);
            }
        }
        if (Objects.nonNull(password)) {
            String encodedPassword = this.passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
        }
        if (Objects.nonNull(email)) {
            boolean emailIsExists = this.zhemUserRepository.existsByEmail(email);
            if (emailIsExists && !user.getEmail().equals(email)) {
                throw new ZhemUserWithDuplicateEmailException("User with this email is already exists");
            } else {
                user.setEmail(email);
            }
        }
        if (Objects.nonNull(firstName)) {
            user.setFirstName(firstName);
        }
        if (Objects.nonNull(lastName)) {
            user.setLastName(lastName);
        }

        return this.zhemUserRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(long userId) {
        boolean isExists = this.zhemUserRepository.existsById(userId);
        if (isExists) {
            this.zhemUserRepository.deleteById(userId);
        } else {
            throw new ZhemUserNotFoundException("User not found");
        }
    }
}
