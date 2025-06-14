package ru.services.billing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Account create request", example = "{\"id\": 1, \"username\": \"alex\"}")
public record AccountRequest(
        @Schema(description = "Account ID", example = "1")
        Long id,
        @Schema(description = "Username", example = "alex")
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
        String username){
}