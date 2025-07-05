package ru.core.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ReserveProductCommand extends Command {
    String username;
    Long orderId;
    Long productId;
    Integer quantity;
}
