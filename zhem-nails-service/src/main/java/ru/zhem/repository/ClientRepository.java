package ru.zhem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zhem.entity.Client;

import java.math.BigDecimal;

@Repository
public interface ClientRepository extends CrudRepository<Client, BigDecimal> {
}
