package ru.services.billing.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Account response", example = "{\"id\": 1, \"username\": \"alex\", \"amount\": \"100.00\"}")
public record AccountResponse(Long id, String username, BigDecimal amount) {
}
