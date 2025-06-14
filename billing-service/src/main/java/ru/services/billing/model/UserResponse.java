package ru.services.billing.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(Long id, String username, String email) {
}
