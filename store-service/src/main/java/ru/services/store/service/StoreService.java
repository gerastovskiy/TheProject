package ru.services.store.service;

import ru.services.store.model.Product;

public interface StoreService {
    Product increaseProduct(Long productId, Integer quantity);
    Product decreaseProduct(Long productId, Integer quantity);
}