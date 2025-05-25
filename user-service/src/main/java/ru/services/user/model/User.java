package ru.services.user.model;

public record User(Long id, String username, String email, UserRole role) {
}