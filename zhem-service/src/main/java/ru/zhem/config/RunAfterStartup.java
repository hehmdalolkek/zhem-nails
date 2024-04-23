package ru.zhem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.zhem.entity.Role;
import ru.zhem.repository.RoleRepository;
import ru.zhem.service.shedule.DatabaseCleanupService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class RunAfterStartup {

    private final RoleRepository roleRepository;

    private final DatabaseCleanupService databaseCleanupService;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        createRolesIfNotExist();
        databaseCleanupService.cleanupIntervalsFromDatabase();
    }

    private void createRolesIfNotExist() {
        boolean adminIsExists = this.roleRepository.existsByTitleIgnoreCase("ADMIN");
        if (!adminIsExists) {
            this.roleRepository.save(Role.builder().title("ADMIN").build());
            System.out.println(LocalDateTime.now() + " --- Admin role created successfully.");
        }
        boolean clientIsExists = this.roleRepository.existsByTitleIgnoreCase("CLIENT");
        if (!clientIsExists) {
            this.roleRepository.save(Role.builder().title("CLIENT").build());
            System.out.println(LocalDateTime.now() + " --- Client role created successfully.");
        }
    }

}