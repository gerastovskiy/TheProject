package ru.services.notification.exception;

import ru.services.notification.model.NotificationType;

public class UnsupportedNotificationTypeException extends Throwable {
    public UnsupportedNotificationTypeException(NotificationType type) {
    }
}
