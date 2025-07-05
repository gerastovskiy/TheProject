package ru.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderApprovedEvent extends Event {
    String username;
    Long orderId;
    BigDecimal amount;
    Long productId;
    Integer quantity;
}
