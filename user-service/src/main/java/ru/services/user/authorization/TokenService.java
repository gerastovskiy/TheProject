package ru.services.user.authorization;

public interface TokenService {
    TokenValidationResult validateToken(String token);
}
