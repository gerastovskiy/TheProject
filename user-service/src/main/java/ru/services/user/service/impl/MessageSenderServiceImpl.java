package ru.services.user.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.core.commands.CreateBillingAccountCommand;
import ru.services.user.service.MessageSenderService;

@Service
public class MessageSenderServiceImpl implements MessageSenderService {
    @Value("${rabbitmq.billing-routing-key.name}")
    private String billingRoutingKey;
    @Value("${rabbitmq.notification-routing-key.name}")
    private String notificationRoutingKey;
    private final RabbitTemplate rabbitTemplate;

    public MessageSenderServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendBillingMessage(CreateBillingAccountCommand command) {
        rabbitTemplate.convertAndSend(billingRoutingKey, command);
    }

    @Override
    public void sendNotificationMessage(Object message) {
        rabbitTemplate.convertAndSend(notificationRoutingKey, message);
    }
}
