package ru.services.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.services.notification.entity.NotificationEntity;
import ru.services.notification.mapper.NotificationMapper;
import ru.services.notification.model.Notification;
import ru.services.notification.repository.NotificationRepository;
import ru.services.notification.service.NotificationService;

@Transactional
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper mapper;

    @Override
    public void createNotification(Notification notification) {
        NotificationEntity notificationEntity = mapper.toNotificationEntity(notification);
        notificationRepository.save(notificationEntity);
    }
}