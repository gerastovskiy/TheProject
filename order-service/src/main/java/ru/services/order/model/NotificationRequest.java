package ru.services.order.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class NotificationRequest{
        String username;
        @Enumerated(EnumType.STRING)
        NotificationType type;
        String message;
}