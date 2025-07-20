package ru.services.order.exception;

public class OrderAlreadyCreated extends RuntimeException{
    public OrderAlreadyCreated(String idempotencyKey) {
        super("Order already created with idempotency key: " + idempotencyKey);
    }
}
