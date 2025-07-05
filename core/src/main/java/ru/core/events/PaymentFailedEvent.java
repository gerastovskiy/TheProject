package ru.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PaymentFailedEvent extends Event {
    String username;
    Long orderId;
    String error;
}
