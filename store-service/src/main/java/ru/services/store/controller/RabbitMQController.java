package ru.services.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.core.commands.*;
import ru.services.store.mapper.ProductMapper;
import ru.services.store.service.StoreService;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = "#{@environment.getProperty('rabbitmq.queue.name')}")
public class RabbitMQController {
    @Value("${rabbitmq.processed-routing-key.name}")
    private String processedRoutingKey;
    private final RabbitTemplate rabbitTemplate;
    private final StoreService storeService;
    private final ProductMapper mapper;

    @RabbitHandler
    public void handleReserveProductCommand(ReserveProductCommand command) {
        // TODO: реализовать обработку ошибок
        try {
            var product = storeService.decreaseProduct(command.getProductId(), command.getQuantity());
            rabbitTemplate.convertAndSend(processedRoutingKey, mapper.toProductReservedEvent(command.getOrderId()));
        }
        catch (Exception e) {
            rabbitTemplate.convertAndSend(processedRoutingKey, mapper.toProductReservationFailedEvent(command.getOrderId(), e.getMessage()));
        }
    }

    @RabbitHandler
    public void handleCancelReserveProductCommand(CancelReserveProductCommand command) {
        // TODO: реализовать обработку ошибок
        try {
            var product = storeService.increaseProduct(command.getProductId(), command.getQuantity());
            rabbitTemplate.convertAndSend(processedRoutingKey, mapper.toProductReservationCencelledEvent(command.getOrderId()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
