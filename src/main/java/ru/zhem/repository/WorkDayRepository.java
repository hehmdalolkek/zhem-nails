package ru.zhem.repository;

import org.springframework.data.repository.CrudRepository;
import ru.zhem.entity.WorkDay;

public interface WorkDayRepository extends CrudRepository<WorkDay, Long> {
}
