package ru.zhem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("select u from ZhemUser u left join u.roles r where u.phone = :phone and r.title = :role")
    Optional<ZhemUser> findByPhoneAndRole(@Param("phone") String phone, @Param("role") String role);

    @EntityGraph(value = "zhem-user_entity-graph")
    List<ZhemUser> findAllByRolesContains(Role role);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    @EntityGraph(value = "zhem-user_entity-graph")
    @Override
    List<ZhemUser> findAll();

    @Query("from ZhemUser u left join u.roles r where not exists (select 1 from u.roles r2 where r2.title = :role)")
    Page<ZhemUser> findAllByRolesTitleNotContains(Pageable pageable, String role);

    @EntityGraph(value = "zhem-user_entity-graph")
    List<ZhemUser> findAll(Specification<ZhemUser> specification);

    Boolean existsByRolesTitleContainingIgnoreCase(String role);

    @EntityGraph(value = "zhem-user_entity-graph")
    Optional<ZhemUser> findByRolesTitleContainingIgnoreCase(String role);
}
