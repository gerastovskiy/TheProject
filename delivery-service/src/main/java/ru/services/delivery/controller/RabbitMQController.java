package ru.services.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.core.commands.*;
import ru.services.delivery.mapper.DeliveryMapper;
import ru.services.delivery.service.StoreService;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = "#{@environment.getProperty('rabbitmq.queue.name')}")
public class RabbitMQController {
    @Value("${rabbitmq.processed-routing-key.name}")
    private String processedRoutingKey;
    private final RabbitTemplate rabbitTemplate;
    private final StoreService storeService;
    private final DeliveryMapper mapper;

    @RabbitHandler
    public void handleReserveProductCommand(ReserveDeliveryCommand command) {
        // TODO: реализовать обработку ошибок
        try {
            var product = storeService.reservedDelivery(mapper.toDelivery(command.getOrderId()));
            rabbitTemplate.convertAndSend(processedRoutingKey, mapper.toDeliveryReservedEvent(command.getOrderId()));
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            rabbitTemplate.convertAndSend(processedRoutingKey, mapper.toDeliveryReservationFailedEvent(command.getOrderId(), e.getMessage()));
        }
    }

    @RabbitHandler
    public void handleCancelReserveProductCommand(CancelReserveDeliveryCommand command) {
        // TODO: реализовать обработку ошибок
        try {
            // TODO: реализовать отмену доставки
            rabbitTemplate.convertAndSend(processedRoutingKey, mapper.toDeliveryReservationCalcelEvent(command.getOrderId()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
