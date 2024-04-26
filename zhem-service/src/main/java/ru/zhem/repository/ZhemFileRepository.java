package ru.zhem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.ZhemFile;

@Repository
public interface ZhemFileRepository extends JpaRepository<ZhemFile, Long> {
}
