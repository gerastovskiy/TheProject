package ru.services.store.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.services.store.entity.ProductEntity;
import ru.services.store.exception.InsufficientQuantityException;
import ru.services.store.exception.ProductNotFoundException;
import ru.services.store.mapper.ProductMapper;
import ru.services.store.model.Product;
import ru.services.store.repository.ProductRepository;
import ru.services.store.service.StoreService;

@Transactional
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Override
    public Product increaseProduct(Long productId, Integer quantity) {
        ProductEntity productEntity = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        productEntity.setQuantity(productEntity.getQuantity() + quantity);
        productRepository.save(productEntity);

        return mapper.toProduct(productEntity);
    }

    @Override
    public Product decreaseProduct(Long productId, Integer quantity) {
        ProductEntity productEntity = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (productEntity.getQuantity() < quantity) throw new InsufficientQuantityException(quantity);

        productEntity.setQuantity(productEntity.getQuantity() - quantity);
        productRepository.save(productEntity);

        return mapper.toProduct(productEntity);
    }
}