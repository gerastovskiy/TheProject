package ru.services.order.saga;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.core.commands.*;
import ru.core.events.*;
import ru.services.order.mapper.OrderMapper;
import ru.services.order.service.OrderService;

@Service
@RequiredArgsConstructor
@RabbitListener(queues = "#{@environment.getProperty('rabbitmq.queue.name')}")
public class OrderSaga {
    private final OrderService orderService;
    private final OrderMapper mapper;
    @Value("${rabbitmq.order-routing-key.name}")
    private String orderRoutingKey;
    @Value("${rabbitmq.billing-routing-key.name}")
    private String billingRoutingKey;
    @Value("${rabbitmq.store-routing-key.name}")
    private String storeRoutingKey;
    @Value("${rabbitmq.delivery-routing-key.name}")
    private String deliveryRoutingKey;
    @Value("${rabbitmq.notification-routing-key.name}")
    private String notificationRoutingKey;
    private final RabbitTemplate rabbitTemplate;

    // events
    // good
    // 1 order service event
    @RabbitHandler
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(billingRoutingKey, mapper.toProcessPaymentCommand(event));
    }

    // 2 payment service event
    @RabbitHandler
    public void handlePaymentProcessedEvent(PaymentProcessedEvent event) {
        var orderCreatedEvent = mapper.toOrderCreatedEvent(orderService.getOrder(event.getOrderId()));
        rabbitTemplate.convertAndSend(storeRoutingKey, mapper.toReserveProductCommand(orderCreatedEvent));
    }

    // 3 product service event
    @RabbitHandler
    public void handleProductReservedEvent(ProductReservedEvent event) {
        var orderCreatedEvent = mapper.toOrderCreatedEvent(orderService.getOrder(event.getOrderId()));
        rabbitTemplate.convertAndSend(deliveryRoutingKey, mapper.toReserveDeliveryCommand(orderCreatedEvent));
    }

    // 4 delivery service event
    @RabbitHandler
    public void handleDeliveryReservedEvent(DeliveryReservedEvent event) {
        var orderCreatedEvent = mapper.toOrderCreatedEvent(orderService.getOrder(event.getOrderId()));
        rabbitTemplate.convertAndSend(orderRoutingKey, mapper.toApproveOrderCommand(orderCreatedEvent));
    }

    // 5 order service final event
    @RabbitHandler
    public void handleOrderApprovedEvent(OrderApprovedEvent event) {
        var orderCreatedEvent = mapper.toOrderCreatedEvent(orderService.getOrder(event.getOrderId()));
        rabbitTemplate.convertAndSend(notificationRoutingKey, mapper.toSendApproveOrderNotificationCommand(orderCreatedEvent));
    }

    // bad
    // 1 delivery service event
    @RabbitHandler
    public void handleEvent(DeliveryReservationFailedEvent event) {
        var orderRejectedEvent  = mapper.toOrderRejectedEvent(orderService.getOrder(event.getOrderId()));
        rabbitTemplate.convertAndSend(storeRoutingKey, mapper.toCancelReserveProductCommand(orderRejectedEvent));
    }

    // 2 product service event
    @RabbitHandler
    public void handleProductReservationFailedEvent(ProductReservationFailedEvent event) {
        var orderRejectedEvent  = mapper.toOrderRejectedEvent(orderService.getOrder(event.getOrderId()));
        rabbitTemplate.convertAndSend(billingRoutingKey, mapper.toCancelPaymentCommand(orderRejectedEvent));
    }

    // 3-1 payment service event
    @RabbitHandler
    public void handlePaymentFailedEvent(PaymentFailedEvent event) {
        var orderRejectedEvent = mapper.toOrderRejectedEvent(orderService.getOrder(event.getOrderId()));
        rabbitTemplate.convertAndSend(orderRoutingKey, mapper.toRejectOrderCommand(orderRejectedEvent));
    }

    // 3-2 payment service event
    @RabbitHandler
    public void handlePaymentFailedEvent(PaymentCalcelledEvent event) {
        var orderRejectedEvent = mapper.toOrderRejectedEvent(orderService.getOrder(event.getOrderId()));
        rabbitTemplate.convertAndSend(orderRoutingKey, mapper.toRejectOrderCommand(orderRejectedEvent));
    }

    // 4 order service final event
    @RabbitHandler
    public void handleOrderRejectedEvent(OrderRejectedEvent event) {
        var orderRejectedEvent = mapper.toOrderRejectedEvent(orderService.getOrder(event.getOrderId()));
        rabbitTemplate.convertAndSend(notificationRoutingKey, mapper.toSendRejectOrderNotificationCommand(orderRejectedEvent));
    }

    // command
    @RabbitHandler
    public void handleApproveOrderCommand(ApproveOrderCommand command) {
        orderService.approveOrder(command.getOrderId());
    }

    @RabbitHandler
    public void handleRejectOrderCommand(RejectOrderCommand command) {
        orderService.rejectOrder(command.getOrderId());
    }


    //TODO: написать обработчик запроса на отзывы заказа
    @RabbitHandler
    public void handleEvent(OrderCreatedRejectEvent event) {
    }
}