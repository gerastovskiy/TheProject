package ru.services.order.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Order response", example = "{\"id\": 1, \"username\": \"alex\", \"amount\": \"100.00\", \"status\": \"CREATED\", \"productId\": \"10\", \"quantity\": \"1\"}")
public record OrderResponse(Long id, String username, BigDecimal amount, OrderStatus status, Long productId, Integer quantity) {
}
