package ru.services.billing.exception;

public class PaymentAlreadyProcessed extends RuntimeException{
    public PaymentAlreadyProcessed(String idempotencyKey) {
        super("Payment already processed with idempotency key: " + idempotencyKey);
    }
}
