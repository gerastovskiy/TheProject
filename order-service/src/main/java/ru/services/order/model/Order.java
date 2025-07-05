package ru.services.order.model;

import lombok.*;
import java.math.BigDecimal;

@Setter
@Getter
@RequiredArgsConstructor
public class Order{
    Long id;
    String username;
    BigDecimal amount;
    OrderStatus status;
    Long productId;
    Integer quantity;
}