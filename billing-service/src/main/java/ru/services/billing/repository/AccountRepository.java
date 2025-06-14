package ru.services.billing.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.services.billing.entity.AccountEntity;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
    boolean existsByUsername(@NonNull String username);
    Optional<AccountEntity> findByUsername(String username);
}