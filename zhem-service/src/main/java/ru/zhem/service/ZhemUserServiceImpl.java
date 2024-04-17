package ru.zhem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.zhem.entity.ZhemUser;
import ru.zhem.exception.ZhemUserNotFoundException;
import ru.zhem.exception.ZhemUserWithDuplicateEmailException;
import ru.zhem.exception.ZhemUserWithDuplicatePhoneException;
import ru.zhem.repository.RoleRepository;
import ru.zhem.repository.ZhemUserRepository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ZhemUserServiceImpl implements ZhemUserService {

    private final ZhemUserRepository zhemUserRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public List<ZhemUser> findAllClients() {
        return this.zhemUserRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .noneMatch(role -> role.getTitle().equals("ADMIN")))
                .toList();
    }

    @Override
    public Page<ZhemUser> findAllClientsByPage(Pageable pageable) {
        Page<ZhemUser> allUsers = zhemUserRepository.findAll(pageable);
        List<ZhemUser> filteredUsers = allUsers.getContent().stream()
                .filter(user -> user.getRoles().stream()
                        .noneMatch(role -> "ADMIN".equalsIgnoreCase(role.getTitle())))
                .toList();

        return new PageImpl<>(filteredUsers, pageable, filteredUsers.size());
    }

    @Override
    @Transactional
    public ZhemUser findUserById(long userId) {
        return this.zhemUserRepository.findById(userId)
                .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public ZhemUser findUserByPhone(String phone, boolean isAdmin) {
        if (isAdmin) {
            return this.zhemUserRepository.findByPhoneAndRolesTitleContainsIgnoreCase(phone, "ADMIN")
                    .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
        } else {
            return this.zhemUserRepository.findByPhone(phone)
                    .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
        }
    }

    @Override
    @Transactional
    public ZhemUser findUserByEmail(String email) {
        return this.zhemUserRepository.findByEmail(email)
                .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public ZhemUser createUser(ZhemUser user) {
        boolean phoneIsExists = this.zhemUserRepository.existsByPhone(user.getPhone());
        if (phoneIsExists) {
            throw new ZhemUserWithDuplicatePhoneException("User with this phone is already exists");
        }
        if (Objects.nonNull(user.getEmail())) {
            boolean emailIsExists = this.zhemUserRepository.existsByEmail(user.getEmail());
            if (emailIsExists) {
                throw new ZhemUserWithDuplicateEmailException("User with this email is already exists");
            }
        }

        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return this.zhemUserRepository.save(user);
    }

    @Override
    @Transactional
    public ZhemUser updateUser(long userId, ZhemUser user) {
        ZhemUser foundedUser = this.zhemUserRepository.findById(userId)
                .orElseThrow(() -> new ZhemUserNotFoundException("User not found"));
        if (Objects.nonNull(user.getPhone())) {
            boolean phoneIsExists = this.zhemUserRepository.existsByPhone(user.getPhone());
            if (phoneIsExists && !foundedUser.getPhone().equals(user.getPhone())) {
                throw new ZhemUserWithDuplicatePhoneException("User with this phone is already exists");
            } else {
                foundedUser.setPhone(user.getPhone());
            }
        }
        if (Objects.nonNull(user.getPassword())) {
            String encodedPassword = this.passwordEncoder.encode(user.getPassword());
            foundedUser.setPassword(encodedPassword);
        }
        if (Objects.nonNull(user.getEmail())) {
            boolean emailIsExists = this.zhemUserRepository.existsByEmail(user.getEmail());
            if (emailIsExists) {
                if (Objects.nonNull(foundedUser.getEmail()) && !foundedUser.getEmail().equals(user.getEmail())) {
                    throw new ZhemUserWithDuplicateEmailException("User with this email is already exists");
                } else if (Objects.isNull(foundedUser.getEmail())) {
                    throw new ZhemUserWithDuplicateEmailException("User with this email is already exists");
                }
            } else {
                foundedUser.setEmail(user.getEmail());
            }
        }
        if (Objects.nonNull(user.getFirstName())) {
            foundedUser.setFirstName(user.getFirstName());
        }
        if (Objects.nonNull(user.getLastName())) {
            foundedUser.setLastName(user.getLastName());
        }

        return this.zhemUserRepository.save(foundedUser);
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

    @Override
    @Transactional
    public List<ZhemUser> findAllClientsBy(String firstName, String lastName, String phone, String email) {
        return this.zhemUserRepository
                .findAllByFirstNameOrLastNameOrPhoneOrEmail(firstName, lastName, phone, email)
                .stream()
                .filter(user -> user.getRoles().stream()
                        .noneMatch(role -> role.getTitle().equals("ADMIN")))
                .toList();
    }

    @Override
    @Transactional
    public Boolean adminIsExists() {
        return this.zhemUserRepository.existsByRolesTitleContainingIgnoreCase("ADMIN");
    }
}
