package ru.services.notification.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.services.notification.model.Notification;
import ru.services.notification.model.NotificationType;

@Service
@ConditionalOnProperty(name = "notification.email.enabled", havingValue = "true")
@NotificationHandler(NotificationType.EMAIL)
public class EmailSender implements NotificationSender {
    private final JavaMailSender mailSender;
    private final String fromEmail;

    public EmailSender(
            JavaMailSender mailSender,
            @Value("${notification.email.from}") String fromEmail
    ) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    @Override
    public void send(Notification message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(message.contact());
        mailMessage.setSubject("Уведомление по заказу");
        mailMessage.setText(message.message());

        mailSender.send(mailMessage);
    }
}
