package ru.zhem.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.zhem.entity.WorkDay;

import java.time.Month;
import java.time.Year;

@Repository
public interface WorkDayRepository extends CrudRepository<WorkDay, Long> {

    @Transactional
    @Query("select distinct workDay from WorkDay workDay where year(workDay.date) = :year and month(workDay.date) = :month")
    Iterable<WorkDay> findWorkDaysByDate(@Param("year") int year, @Param("month") short month);

    @Transactional
    @Query("select distinct workDay from WorkDay workDay left join WorkInterval workInterval on workDay.id = workInterval.workDay.id " +
            "where workInterval.isBooked = false and year(workDay.date) = :year and month(workDay.date) = :month")
    Iterable<WorkDay> findWorkDaysByNotBookedWorkIntervalAndDate(@Param("year") int year, @Param("month") short month);
}
