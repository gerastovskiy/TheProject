package ru.services.notification.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.services.notification.model.Notification;
import ru.services.notification.model.NotificationType;

@Service
@ConditionalOnProperty(name = "notification.telegram.enabled", havingValue = "true")
@NotificationHandler(NotificationType.TELEGRAM)
public class TelegramSender extends DefaultAbsSender implements NotificationSender {

    public TelegramSender(
            @Value("${notification.telegram.bot-token}") String botToken
    ) {
        super(new DefaultBotOptions(), botToken);
    }

    @Override
    public void send(Notification notification) {
        SendMessage message = new SendMessage();
        message.setChatId(notification.contact());
        message.setText(notification.message());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            handleError(e, notification);
        }
    }

    private void handleError(TelegramApiException e, Notification notification) {
        if (e.getMessage().contains("Too Many Requests")) {
            sleep(1000); // 1 секунда
            send(notification);
        }
        // Для других ошибок просто пробрасываем исключение
        else {
            throw new RuntimeException("Telegram send failed", e);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}