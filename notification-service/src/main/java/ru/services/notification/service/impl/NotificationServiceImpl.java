package ru.services.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.services.notification.entity.NotificationEntity;
import ru.services.notification.exception.UnsupportedNotificationTypeException;
import ru.services.notification.mapper.NotificationMapper;
import ru.services.notification.model.Notification;
import ru.services.notification.repository.NotificationRepository;
import ru.services.notification.sender.NotificationSenderFacade;
import ru.services.notification.service.NotificationService;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper mapper;
    private final NotificationSenderFacade  notificationSenderFacade;

    @Override
    public void sendNotification(Notification request) {
        //TODO: реализовать все способы отправки
        try {
            notificationSenderFacade.sendNotification(request);
        } catch (UnsupportedNotificationTypeException e) {
            throw new RuntimeException(e);
        }

        NotificationEntity notificationEntity = mapper.toNotificationEntity(request);
        notificationRepository.save(notificationEntity);
    }
}