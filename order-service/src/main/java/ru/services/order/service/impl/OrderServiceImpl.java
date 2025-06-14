package ru.services.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.services.order.entity.OrderEntity;
import ru.services.order.exception.OrderNotFoundException;
import ru.services.order.mapper.OrderMapper;
import ru.services.order.model.Order;
import ru.services.order.model.OrderStatus;
import ru.services.order.repository.OrderRepository;
import ru.services.order.service.OrderService;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    @Override
    public Order getOrder(Long id) {
        OrderEntity orderEntity = orderRepository
                .findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return mapper.Order(orderEntity);
    }

    @Override
    public List<Order> getOrdersList(String username) {
        List<OrderEntity> orderEntity = orderRepository
                .findByUsername(username);

        return orderEntity.stream().map(mapper::Order).collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id))
            throw new OrderNotFoundException(id);

        orderRepository.deleteById(id);
    }

    @Override
    public Order createOrder(Order order) {
        OrderEntity orderEntity = mapper.toOrderEntity(order);

        return mapper.Order(orderRepository.save(orderEntity));
    }

    @Override
    public Order setPayedStatus(Long id) {
        OrderEntity orderEntity = orderRepository
                .findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        orderEntity.setStatus(OrderStatus.PAYED);
        orderRepository.save(orderEntity);

        return mapper.Order(orderEntity);
    }
}