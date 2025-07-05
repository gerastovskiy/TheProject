package ru.services.delivery.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.services.delivery.entity.DeliveryEntity;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends CrudRepository<DeliveryEntity, Long> {
    boolean existsByOrderId(@NonNull Long orderId);
}