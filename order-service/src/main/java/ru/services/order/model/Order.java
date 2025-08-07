package ru.services.order.model;

import lombok.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Setter
@Getter
@RequiredArgsConstructor
public class Order{
    Long id;
    UUID uuid;
    String username;
    BigDecimal amount;
    OrderStatus status;
    Long productId;
    Integer quantity;
    String contact;
    NotificationType type;
    private Timestamp created;
    private Timestamp updated;
}