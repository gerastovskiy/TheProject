package ru.services.store.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;
import java.util.UUID;

@Schema(description = "Product response", example = "{\"id\": 1, \"uuid\": \"6d4b37e7-15aa-40a0-bf53-d915a59bd12a\", \"name\": \"phone\", \"quantity\": \"100\"}")
public record ProductResponse(Long id, UUID uuid, String name, Integer quantity, Double price, Boolean active, String description, Timestamp created, Timestamp updated) {
}