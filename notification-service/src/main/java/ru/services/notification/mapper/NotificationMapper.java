package ru.services.notification.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.services.notification.entity.NotificationEntity;
import ru.services.notification.model.Notification;
import ru.services.notification.model.NotificationRequest;
import ru.services.notification.validation.ValidateAfterMapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @ValidateAfterMapping
    @Mapping(target = "id", ignore = true)
    Notification toNotification(NotificationRequest Request);
    NotificationEntity toNotificationEntity(Notification user);
}
