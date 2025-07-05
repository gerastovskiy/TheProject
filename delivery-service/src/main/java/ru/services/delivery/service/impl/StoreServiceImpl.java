package ru.services.delivery.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.services.delivery.exception.DuplicateDeliveryException;
import ru.services.delivery.mapper.DeliveryMapper;
import ru.services.delivery.model.Delivery;
import ru.services.delivery.repository.DeliveryRepository;
import ru.services.delivery.service.StoreService;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Transactional
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper mapper;

    @Override
    public Delivery reservedDelivery(Delivery delivery) {
        if (deliveryRepository.existsByOrderId(delivery.orderId()))
            throw new DuplicateDeliveryException(delivery.orderId());

        var deliveryEntity = mapper.toDeliveryEntity(delivery);
        deliveryEntity.setDate(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));

        deliveryRepository.save(deliveryEntity);
        return mapper.toDelivery(deliveryEntity);
    }
}