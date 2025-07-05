package ru.services.delivery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.core.events.*;
import ru.services.delivery.entity.DeliveryEntity;
import ru.services.delivery.model.Delivery;
import ru.services.delivery.validation.ValidateAfterMapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    @ValidateAfterMapping
    DeliveryEntity toDeliveryEntity(Delivery product);
    @ValidateAfterMapping
    Delivery toDelivery(DeliveryEntity productEntity);
    @ValidateAfterMapping
    @Mapping(target = "orderId", source = "orderId")
    Delivery toDelivery(Long orderId);

    DeliveryReservedEvent toDeliveryReservedEvent(Long orderId);
    DeliveryReservationCalcelEvent toDeliveryReservationCalcelEvent(Long orderId);
    DeliveryReservationFailedEvent toDeliveryReservationFailedEvent(Long orderId, String error);
}