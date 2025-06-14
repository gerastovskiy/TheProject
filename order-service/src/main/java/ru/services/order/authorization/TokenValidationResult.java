package ru.services.order.authorization;

public record TokenValidationResult (boolean isValid, String client_id) {}