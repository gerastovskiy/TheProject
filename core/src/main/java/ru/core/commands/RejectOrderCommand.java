package ru.core.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class RejectOrderCommand extends Command {
    String username;
    Long orderId;
}
