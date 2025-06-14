package ru.services.order.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.services.order.model.NotificationRequest;
import ru.services.order.service.MessageSenderService;

@Service
public class MessageSenderServiceImpl implements MessageSenderService {
    @Value("${rabbitmq.notification-routing-key.name}")
    private String notificationRoutingKey;
    private final RabbitTemplate rabbitTemplate;

    public MessageSenderServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendNotificationMessage(Object message) {
        rabbitTemplate.convertAndSend(notificationRoutingKey, message);
    }
}
