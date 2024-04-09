package ru.zhem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.entity.Role;
import ru.zhem.exception.RoleWithDuplicateTitleException;
import ru.zhem.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Role createRole(Role role) {
        boolean isExists = this.roleRepository.existsByTitleIgnoreCase(role.getTitle());
        if (isExists) {
            throw new RoleWithDuplicateTitleException("Role with this title is already exists");
        }
        return this.roleRepository.save(Role.builder()
                .title(role.getTitle().toUpperCase())
                .build());
    }
}
