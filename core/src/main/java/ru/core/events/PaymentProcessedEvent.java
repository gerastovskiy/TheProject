package ru.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PaymentProcessedEvent extends Event {
    String username;
    Long orderId;
}