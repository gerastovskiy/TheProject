package ru.services.notification.sender;

import ru.services.notification.model.Notification;

public interface NotificationSender {
    void send(Notification message);
}
