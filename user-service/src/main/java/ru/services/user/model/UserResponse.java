package ru.services.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User response", example = "{\"id\": 1, \"username\": \"alex\", \"email\": \"alex@example.com\"}")
public record UserResponse(Long id, String username, String email) {
}
