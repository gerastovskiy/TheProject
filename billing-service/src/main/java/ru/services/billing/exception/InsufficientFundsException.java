package ru.services.billing.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(BigDecimal amount) {
        super("Amount " + amount + " is insufficient funds");
    }
}