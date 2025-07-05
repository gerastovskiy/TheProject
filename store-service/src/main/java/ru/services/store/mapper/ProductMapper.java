package ru.services.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.core.events.*;
import ru.services.store.entity.ProductEntity;
import ru.services.store.model.Product;
import ru.services.store.validation.ValidateAfterMapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @ValidateAfterMapping
    ProductEntity toProductEntity(Product product);
    @ValidateAfterMapping
    Product toProduct(ProductEntity productEntity);

    ProductReservedEvent toProductReservedEvent(Long orderId);
    ProductReservationCencelledEvent toProductReservationCencelledEvent(Long orderId);
    ProductReservationFailedEvent toProductReservationFailedEvent(Long orderId, String error);
}