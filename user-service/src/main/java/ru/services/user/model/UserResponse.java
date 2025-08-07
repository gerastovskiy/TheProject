package ru.services.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User response", example = "{\"id\": 1, \"firstName\": \"Ivan\", \"lastName\": \"Ivanov\", \"username\": \"alex\", \"email\": \"alex@example.com\", \"phone\": \"+79999999999\", \"telegram\": \"99999999\", \"address\": \"Москва, Красная Площадь, д.1\"}")

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        String phone,
        Integer telegram,
        String address
) {
}
