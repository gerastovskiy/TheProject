package ru.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderCreatedEvent extends Event {
    String username;
    Long orderId;
    BigDecimal amount;
    Long productId;
    Integer quantity;
}