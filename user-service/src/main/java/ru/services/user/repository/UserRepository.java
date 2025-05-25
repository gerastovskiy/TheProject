package ru.services.user.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.services.user.entity.UserEntity;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    void deleteByUsername(@NonNull String username);
    boolean existsByUsername(@NonNull String username);
    Optional<UserEntity> findByUsername(String username);
}