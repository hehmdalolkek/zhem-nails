package ru.zhem.repository;

import org.springframework.data.repository.CrudRepository;
import ru.zhem.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
