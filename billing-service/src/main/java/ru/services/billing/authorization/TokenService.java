package ru.services.billing.authorization;

public interface TokenService {
    TokenValidationResult validateToken(String token);
}
