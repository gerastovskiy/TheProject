package ru.services.billing.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super("Account not found with id: " + id);
    }

    public AccountNotFoundException(String username) {
        super("Account not found with username: " + username);
    }
}