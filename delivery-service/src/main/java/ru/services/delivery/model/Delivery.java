package ru.services.delivery.model;

import java.sql.Timestamp;

public record Delivery(Long id, Long orderId, Timestamp date) {
}