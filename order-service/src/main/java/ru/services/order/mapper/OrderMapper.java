package ru.services.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
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
            @Mapping(target = "status", constant = "CREATED")
    })
    Order toOrder(OrderRequest Request);
    OrderEntity toOrderEntity(Order order);
    Order Order(OrderEntity orderEntity);
    OrderResponse toResponse(Order order);
}