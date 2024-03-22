package ru.zhem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.WorkInterval;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkIntervalRepository extends CrudRepository<WorkInterval, Long> {
    Iterable<WorkInterval> findAllByOrderByDateAscStartTime();

    List<WorkInterval> findWorkIntervalsByIsBookedIsFalseAndDateGreaterThanOrderByStartTime(LocalDate date);
}
