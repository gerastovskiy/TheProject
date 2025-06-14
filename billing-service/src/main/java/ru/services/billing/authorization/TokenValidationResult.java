package ru.services.billing.authorization;

public record TokenValidationResult (boolean isValid, String client_id) {}