package ru.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderRejectedEvent {
    String username;
    Long orderId;
    BigDecimal amount;
    Long productId;
    Integer quantity;
    String description;
}
