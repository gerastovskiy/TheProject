package ru.services.user.model;

public record User(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        String phone,
        Integer telegram,
        String address,
        UserRole role) {
}