package ru.services.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.core.commands.*;
import ru.core.events.*;
import ru.services.order.entity.OrderEntity;
import ru.services.order.model.Order;
import ru.services.order.model.OrderRequest;
import ru.services.order.model.OrderResponse;
import ru.services.order.validation.ValidateAfterMapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @ValidateAfterMapping
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "uuid", ignore = true),
            @Mapping(target = "status", constant = "CREATED"),
            @Mapping(target = "created", ignore = true),
            @Mapping(target = "updated", ignore = true)
    })
    Order toOrder(OrderRequest Request);
    OrderEntity toOrderEntity(Order order);
    Order toOrder(OrderEntity orderEntity);
    OrderResponse toResponse(Order order);

    @Mapping(target = "orderId", source = "id")
    OrderCreatedEvent toOrderCreatedEvent(Order order);
    @Mapping(target = "orderId", source = "id")
    OrderCreatedRejectEvent toOrderCreatedRejectEvent(Order order);

    @Mappings({
            @Mapping(target = "orderId", source = "id"),
            @Mapping(target = "description", ignore = true)
    })
    OrderRejectedEvent toOrderRejectedEvent(Order order);
    @Mappings({
        @Mapping(target = "orderId", source = "id"),
    })
    OrderApprovedEvent toOrderApprovedEvent(Order order);

    // order
    ProcessPaymentCommand toProcessPaymentCommand(OrderCreatedEvent event);
    ReserveProductCommand toReserveProductCommand(OrderCreatedEvent event);
    ReserveDeliveryCommand toReserveDeliveryCommand(OrderCreatedEvent event);
    ApproveOrderCommand toApproveOrderCommand(OrderCreatedEvent event);
    SendApproveOrderNotificationCommand toSendApproveOrderNotificationCommand(OrderCreatedEvent event);

    // order cancel
    CancelReserveProductCommand toCancelReserveProductCommand(OrderRejectedEvent event);
    CancelPaymentCommand toCancelPaymentCommand(OrderRejectedEvent event);
    RejectOrderCommand toRejectOrderCommand(OrderRejectedEvent event);
    SendRejectOrderNotificationCommand toSendRejectOrderNotificationCommand(OrderRejectedEvent event);
}
