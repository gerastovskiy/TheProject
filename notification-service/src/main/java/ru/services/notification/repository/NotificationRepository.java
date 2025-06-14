package ru.services.notification.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.services.notification.entity.NotificationEntity;

@Repository
public interface NotificationRepository extends CrudRepository<NotificationEntity, Long> {
}