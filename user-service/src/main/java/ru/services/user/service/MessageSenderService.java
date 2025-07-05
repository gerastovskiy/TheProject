package ru.services.user.service;

import ru.core.commands.CreateBillingAccountCommand;

public interface MessageSenderService {
    void sendBillingMessage(CreateBillingAccountCommand command);
    void sendNotificationMessage(Object message);
}
