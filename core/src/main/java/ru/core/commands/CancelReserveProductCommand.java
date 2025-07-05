package ru.core.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CancelReserveProductCommand extends Command {
    String username;
    Long orderId;
    Long productId;
    Integer quantity;
}