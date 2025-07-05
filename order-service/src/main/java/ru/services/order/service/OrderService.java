package ru.services.order.service;

import ru.services.order.model.Order;
import java.util.List;

public interface OrderService {
    Order getOrder(Long id);
    List<Order> getOrdersList(String username);
    Order createOrder(Order user);
    void createOrderReject(Long id);
    void rejectOrder(Long id);
    void approveOrder(Long id);
}