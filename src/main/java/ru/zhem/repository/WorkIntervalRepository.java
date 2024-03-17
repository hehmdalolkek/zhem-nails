package ru.zhem.repository;

import org.springframework.data.repository.CrudRepository;
import ru.zhem.entity.WorkInterval;

public interface WorkIntervalRepository extends CrudRepository<WorkInterval, Long> {
}
