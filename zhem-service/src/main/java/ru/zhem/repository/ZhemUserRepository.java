package ru.zhem.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.Role;
import ru.zhem.entity.ZhemUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZhemUserRepository extends JpaRepository<ZhemUser, Long> {

    @EntityGraph(value = "zhem-user_entity-graph")
    @Override
    Optional<ZhemUser> findById(Long userId);

    @EntityGraph(value = "zhem-user_entity-graph")
    Optional<ZhemUser> findByEmail(String email);

    @EntityGraph(value = "zhem-user_entity-graph")
    Optional<ZhemUser> findByPhone(String phone);

    @EntityGraph(value = "zhem-user_entity-graph")
    Optional<ZhemUser> findByPhoneAndRolesTitleContainsIgnoreCase(String phone, String role);

    @EntityGraph(value = "zhem-user_entity-graph")
    List<ZhemUser> findAllByRolesContains(Role role);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    @EntityGraph(value = "zhem-user_entity-graph")
    @Override
    List<ZhemUser> findAll();
}
