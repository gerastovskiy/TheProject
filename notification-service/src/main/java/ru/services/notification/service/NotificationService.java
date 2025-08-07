package ru.services.notification.service;

import ru.services.notification.model.Notification;

public interface NotificationService {
    void sendNotification(Notification user);
}