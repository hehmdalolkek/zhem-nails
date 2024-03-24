package ru.zhem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.User;

import java.math.BigDecimal;

@Repository
public interface UserRepository extends CrudRepository<User, BigDecimal> {
}
