package ru.zhem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zhem.entity.ZhemService;

public interface ZhemServiceRepository extends JpaRepository<ZhemService, Integer> {
    boolean existsByTitle(String title);
}
