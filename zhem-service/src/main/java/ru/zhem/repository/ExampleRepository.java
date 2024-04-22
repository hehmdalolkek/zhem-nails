package ru.zhem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zhem.entity.Example;

public interface ExampleRepository extends JpaRepository<Example, Long> {
}
