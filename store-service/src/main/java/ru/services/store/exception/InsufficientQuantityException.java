package ru.services.store.exception;

import java.math.BigDecimal;

public class InsufficientQuantityException extends RuntimeException{
    public InsufficientQuantityException(Integer quantity) {
        super("Quantity " + quantity + " is insufficient");
    }
}