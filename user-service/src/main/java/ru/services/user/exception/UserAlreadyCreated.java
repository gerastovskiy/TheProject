package ru.services.user.exception;

public class UserAlreadyCreated extends RuntimeException{
    public UserAlreadyCreated(String idempotencyKey) {
        super("User already created with idempotency key: " + idempotencyKey);
    }
}
