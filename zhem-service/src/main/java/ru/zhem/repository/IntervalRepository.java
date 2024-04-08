package ru.zhem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.Interval;

@Repository
public interface IntervalRepository extends JpaRepository<Interval, Long> {
}
