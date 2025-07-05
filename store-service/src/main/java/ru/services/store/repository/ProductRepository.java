package ru.services.store.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.services.store.entity.ProductEntity;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
}