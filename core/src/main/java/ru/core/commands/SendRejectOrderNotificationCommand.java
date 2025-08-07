package ru.core.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.core.common.NotificationType;

@Getter
@Setter
@RequiredArgsConstructor
public class SendRejectOrderNotificationCommand extends Command {
    String username;
    Long orderId;
    String contact;
    NotificationType type;
}
