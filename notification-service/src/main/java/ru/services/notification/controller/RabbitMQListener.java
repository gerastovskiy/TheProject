package ru.services.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.services.notification.mapper.NotificationMapper;
import ru.services.notification.model.NotificationRequest;
import ru.services.notification.service.NotificationService;

@Component
@RequiredArgsConstructor
public class RabbitMQListener {
    private final NotificationService notificationService;
    private final NotificationMapper mapper;

    @RabbitListener(queues = "#{@environment.getProperty('rabbitmq.queue.name')}")
    public void receiveMessage(NotificationRequest request) {
        // TODO: реализовать обработку ошибок
        try {
            notificationService.createNotification(mapper.toNotification(request));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
