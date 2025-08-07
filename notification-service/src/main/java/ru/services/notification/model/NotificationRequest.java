package ru.services.notification.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Notification create request", example = "{\"username\": \"alex\", \"contact\": \"alex@example.com\", \"type\": \"push\", \"message\": \"Hello!\"}")
public record NotificationRequest(
        @Schema(description = "Username", example = "alex")
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
        String username,
        @Schema(description = "Contact", example = "alex@example.com")
        @NotBlank(message = "Contact cannot be empty")
        String contact,
        @Enumerated(EnumType.STRING)
        NotificationType type,
        @NotBlank
        String message){
}