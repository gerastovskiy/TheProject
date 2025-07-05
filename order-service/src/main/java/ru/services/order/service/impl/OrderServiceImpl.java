package ru.services.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${rabbitmq.order-routing-key.name}")
    private String orderRoutingKey;
    private final RabbitTemplate rabbitTemplate;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    //@Value("${service.billing.url}")
    //private String billingUrl;
    //private final RestClient restClient;

    @Override
    public Order getOrder(Long id) {
        OrderEntity orderEntity = orderRepository
                .findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return mapper.toOrder(orderEntity);
    }

    @Override
    public List<Order> getOrdersList(String username) {
        List<OrderEntity> orderEntity = orderRepository
                .findByUsername(username);

        return orderEntity.stream().map(mapper::toOrder).collect(Collectors.toList());
    }

    @Override
    public Order createOrder(Order order) {
        OrderEntity orderEntity = mapper.toOrderEntity(order);
        orderRepository.save(orderEntity);

        order = mapper.toOrder(orderEntity);
        rabbitTemplate.convertAndSend(orderRoutingKey, mapper.toOrderCreatedEvent(order));
            return order;

/*
        // замена синхронного вызова на асинхронный
        var uri = UriComponentsBuilder
                .fromUriString(billingUrl)
                .path("/billing/credit")
                .queryParam("username", order.getUsername())
                .queryParam("amount", order.getAmount())
                .build().toUriString();

        try {
            var response = restClient.put()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                    .retrieve()
                    .toEntity(String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                order.setStatus(OrderStatus.PAYED);
                orderService.setPayedStatus(order.getId());

                notification.setMessage("Order is created and has been payed");
            }
            else
                notification.setMessage("Order is created but not payed");

        } catch (RestClientResponseException e) {
            System.out.println(e.getResponseBodyAsString());
            notification.setMessage("Order is created but not payed");
        }
*/
    }

    @Override
    public void createOrderReject(Long id) {
        if (!orderRepository.existsById(id))
            throw new OrderNotFoundException(id);

        OrderEntity orderEntity = orderRepository.getOrderEntityById(id);
        rabbitTemplate.convertAndSend(orderRoutingKey, mapper.toOrderCreatedRejectEvent(mapper.toOrder(orderEntity)));
    }

    @Override
    public void rejectOrder(Long id) {
        OrderEntity orderEntity = orderRepository
                .findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        orderEntity.setStatus(OrderStatus.REJECTED);
        orderRepository.save(orderEntity);

        rabbitTemplate.convertAndSend(orderRoutingKey, mapper.toOrderRejectedEvent(mapper.toOrder(orderEntity)));
    }

    @Override
    public void approveOrder(Long id) {
        OrderEntity orderEntity = orderRepository
                .findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        orderEntity.setStatus(OrderStatus.APPROVED);
        orderRepository.save(orderEntity);

        rabbitTemplate.convertAndSend(orderRoutingKey, mapper.toOrderApprovedEvent(mapper.toOrder(orderEntity)));
    }
}