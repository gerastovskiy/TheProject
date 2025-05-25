package ru.services.auth.service;

public interface TokenService {
    String generateToken(String clientId);
}
