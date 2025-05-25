package ru.services.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "User create or update request", example = "{\"id\": 1, \"username\": \"alex\", \"email\": \"alex@example.com\"}")
public record UserRequest(
        @Schema(description = "User ID", example = "1")
        Long id,
        @Schema(description = "Username", example = "alex")
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
        String username,
        @Schema(description = "Email", example = "alex@example.com")
        @Email(message = "Email is invalid")
        @NotBlank(message = "Email cannot be empty")
        String email){
}