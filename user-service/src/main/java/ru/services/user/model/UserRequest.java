package ru.services.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "User create or update request", example = "{\"id\": 1, \"firstName\": \"Ivan\", \"lastName\": \"Ivanov\", \"username\": \"alex\", \"email\": \"alex@example.com\", \"phone\": \"+79999999999\", \"telegram\": \"99999999\", \"address\": \"Москва, Красная Площадь, д.1\"}")
public record UserRequest(
        @Schema(description = "User ID", example = "1")
        Long id,
        @Schema(description = "FirstName", example = "Ivan")
        @NotBlank(message = "FirstName cannot be empty")
        @NotBlank
        String firstName,
        @Schema(description = "LastName", example = "Ivanov")
        @NotBlank(message = "LastName cannot be empty")
        @NotBlank
        String lastName,
        @Schema(description = "Username", example = "alex")
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
        String username,
        @Schema(description = "Email", example = "alex@example.com")
        @Email(message = "Email is invalid")
        @NotBlank(message = "Email cannot be empty")
        String email,
        @Schema(description = "Phone number", example = "+79999999999")
        @NotBlank(message = "Phone number cannot be empty")
        String phone,
        @Schema(description = "Phone number", example = "99999999")
        Integer telegram,
        @NotBlank(message = "Address cannot be empty")
        @Schema(description = "Address", example = "Москва, Красная Площадь, д.1")
        String address){
}