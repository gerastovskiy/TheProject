package ru.services.store.model;

import java.sql.Timestamp;
import java.util.UUID;

public record Product(Long id, UUID uuid, String name, Integer quantity, Double price, Boolean active, String description, Timestamp created, Timestamp updated) {
}