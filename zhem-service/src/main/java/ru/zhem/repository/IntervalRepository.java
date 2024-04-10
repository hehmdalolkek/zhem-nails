package ru.zhem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.Interval;
import ru.zhem.entity.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface IntervalRepository extends JpaRepository<Interval, Long> {

    @Query("from Interval i where extract(year from i.date) = :year and extract(month from i.date) = :month")
    List<Interval> findAllByDateOrderByDateAscTimeDesc(@Param("year") Integer year, @Param("month") Integer month);

    @Query("from Interval i where extract(year from i.date) = :year and extract(month from i.date) = :month" +
            " and i.status = :status")
    List<Interval> findAllByDateAndStatusOrderByDateAscTimeDesc(@Param("year") Integer year,
                                                                @Param("month") Integer month,
                                                                @Param("status") Status status);

    boolean existsByDateAndTime(LocalDate date, LocalTime time);

    boolean existsByDateAndTimeAndIdNot(LocalDate date, LocalTime time, long intervalId);

    @Modifying
    @Query("delete from Interval i where i.date <= :date and i.status = :status")
    void deleteOldAvailableIntervals(@Param("date") LocalDate date,
                                     @Param("status") Status status);
}
