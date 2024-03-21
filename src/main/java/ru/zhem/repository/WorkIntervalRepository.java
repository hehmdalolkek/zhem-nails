package ru.zhem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.WorkInterval;

@Repository
public interface WorkIntervalRepository extends CrudRepository<WorkInterval, Long> {
    Iterable<WorkInterval> findAllByOrderByDateDescStartTimeAsc();
}
