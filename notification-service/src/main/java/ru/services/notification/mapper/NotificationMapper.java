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
            @Mapping(target = "created",
                    expression = "java(java.sql.Timestamp.from(java.time.Instant.now()))"),
            @Mapping(target = "message",
                    expression = "java(\"Поздравляем, \" + command.getUsername() + \", ваш заказ номер \" + command.getOrderId() + \" оформлен и скоро будет доставлен.\")")
    })
    Notification toNotification(SendApproveOrderNotificationCommand command);

    @ValidateAfterMapping
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "created",
                    expression = "java(java.sql.Timestamp.from(java.time.Instant.now()))"),
            @Mapping(target = "message",
                    expression = "java(\"Сожалеем, \" + command.getUsername() + \", ваш заказ номер \" + command.getOrderId() + \" отклонён.\")")
    })
    Notification toNotification(SendRejectOrderNotificationCommand command);
    NotificationEntity toNotificationEntity(Notification user);
}
