package ru.zhem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.WorkDay;
import ru.zhem.entity.WorkInterval;

@Repository
public interface WorkIntervalRepository extends CrudRepository<WorkInterval, Long> {

    Iterable<WorkInterval> findWorkIntervalByWorkDay(WorkDay workDay);

    Iterable<WorkInterval> findWorkIntervalsByIsBookedIsFalse();

    Iterable<WorkInterval> findWorkIntervalsByWorkDayAndIsBookedIsFalse(WorkDay workDay);
}
