package ru.core.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class CancelPaymentCommand {
    String username;
    Long orderId;
    BigDecimal amount;
}
