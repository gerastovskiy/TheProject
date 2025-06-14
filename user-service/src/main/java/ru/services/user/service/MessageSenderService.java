package ru.services.user.service;

public interface MessageSenderService {
    void sendBillingMessage(Object message);
    void sendNotificationMessage(Object message);
}
