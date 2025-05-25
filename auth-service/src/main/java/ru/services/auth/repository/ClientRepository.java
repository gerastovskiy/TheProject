package ru.services.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.services.auth.entity.ClientEntity;

@Repository
public interface ClientRepository extends CrudRepository<ClientEntity, String> {
}