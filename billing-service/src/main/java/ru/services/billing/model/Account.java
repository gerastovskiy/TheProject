package ru.services.billing.model;

import java.math.BigDecimal;

public record Account(Long id, String username, BigDecimal amount) {
}