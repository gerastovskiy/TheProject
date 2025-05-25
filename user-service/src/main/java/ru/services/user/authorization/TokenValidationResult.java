package ru.services.user.authorization;

public record TokenValidationResult (boolean isValid, String client_id) {}