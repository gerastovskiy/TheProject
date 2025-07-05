package ru.services.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.core.commands.*;
import ru.services.notification.mapper.NotificationMapper;
import ru.services.notification.service.NotificationService;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = "#{@environment.getProperty('rabbitmq.queue.name')}")
public class RabbitMQListener {
    private final NotificationService notificationService;
    private final NotificationMapper mapper;

    @RabbitHandler
    public void handleSendApproveOrderNotificationCommand(SendApproveOrderNotificationCommand command) {
        // TODO: реализовать обработку ошибок
        try {
            notificationService.createNotification(mapper.toNotification(command));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitHandler
    public void handleSendRejectOrderNotificationCommand(SendRejectOrderNotificationCommand command) {
        // TODO: реализовать обработку ошибок
        try {
            notificationService.createNotification(mapper.toNotification(command));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
