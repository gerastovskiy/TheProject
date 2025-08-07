package ru.services.notification.sender;

import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.services.notification.exception.UnsupportedNotificationTypeException;
import ru.services.notification.model.Notification;
import ru.services.notification.model.NotificationType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationSenderFacade {
    private final Map<NotificationType, NotificationSender> senders;

    public NotificationSenderFacade(List<NotificationSender> senderList) {
        this.senders = senderList.stream()
                .collect(Collectors.toMap(
                        sender -> sender.getClass().getAnnotation(NotificationHandler.class).value(),
                        Function.identity()
                ));
    }

    @Async
    public void sendNotification(Notification message) throws UnsupportedNotificationTypeException {
        NotificationSender sender = senders.get(message.type());
        if (sender == null) {
            throw new UnsupportedNotificationTypeException(message.type());
        }

        sender.send(message);
    }
}
