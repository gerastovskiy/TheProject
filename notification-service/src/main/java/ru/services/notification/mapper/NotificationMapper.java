package ru.services.notification.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.core.commands.*;
import ru.services.notification.entity.NotificationEntity;
import ru.services.notification.model.Notification;
import ru.services.notification.validation.ValidateAfterMapping;


@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @ValidateAfterMapping
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "type", constant = "PUSH"),
            @Mapping(target = "message", constant = "Order approved!")
    })
    Notification toNotification(SendApproveOrderNotificationCommand command);

    @ValidateAfterMapping
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "type", constant = "PUSH"),
            @Mapping(target = "message", constant = "Order rejected!")
    })
    Notification toNotification(SendRejectOrderNotificationCommand command);
    NotificationEntity toNotificationEntity(Notification user);
}
