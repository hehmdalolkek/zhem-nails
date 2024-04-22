package ru.zhem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.FileInfo;

@Repository
public interface FileRepository extends JpaRepository<FileInfo, Long> {

    boolean existsByName(String name);

}
