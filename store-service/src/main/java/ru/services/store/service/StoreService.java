package ru.services.store.service;

import ru.services.store.model.Product;
import java.util.List;

public interface StoreService {
    Product increaseProduct(Long productId, Integer quantity);
    Product decreaseProduct(Long productId, Integer quantity);
    List<Product> getProductsList(String name);
}