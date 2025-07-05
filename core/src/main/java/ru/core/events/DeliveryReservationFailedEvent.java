package ru.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DeliveryReservationFailedEvent extends Event {
    Long orderId;
    String error;
}
