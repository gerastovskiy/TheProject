package ru.services.order.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Order create request", example = "{\"username\": \"alex\", \"amount\": \"100.00\", \"productId\": \"10\", \"quantity\": \"1\"}")
public record OrderRequest(
        @Schema(description = "Username", example = "alex")
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
        String username,
        @Positive(message = "Order amount must be positive")
        BigDecimal amount,
        @Positive(message = "Product id must be positive")
        Long productId,
        @Positive(message = "Product quantity must be positive")
        Integer quantity
){
}