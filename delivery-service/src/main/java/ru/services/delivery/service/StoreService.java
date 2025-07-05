package ru.services.delivery.service;

import ru.services.delivery.model.Delivery;

public interface StoreService {
    Delivery reservedDelivery(Delivery delivery);
}