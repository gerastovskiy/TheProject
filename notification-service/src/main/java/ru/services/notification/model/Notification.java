package ru.services.notification.model;

public record Notification(Long id, String username, NotificationType type, String message) {
}