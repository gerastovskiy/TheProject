package ru.core.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SendApproveOrderNotificationCommand extends Command {
    String username;
    Long orderId;
}
