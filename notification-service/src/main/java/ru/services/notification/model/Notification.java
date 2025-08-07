package ru.services.notification.model;

import java.sql.Timestamp;

public record Notification(Long id, String username, String contact, NotificationType type, String message, Timestamp created) {
}