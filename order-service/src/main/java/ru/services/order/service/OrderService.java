package ru.services.order.service;

import ru.services.order.model.Order;
import java.util.List;

public interface OrderService {
    Order getOrder(Long id);
    List<Order> getOrdersList(String username);
    void deleteOrder(Long id);
    Order createOrder(Order user);
    Order setPayedStatus(Long id);
}