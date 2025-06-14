package ru.services.order.authorization;

public interface TokenService {
    TokenValidationResult validateToken(String token);
}
