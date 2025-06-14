package ru.services.billing.exception;

public class DuplicateAccountException extends RuntimeException{
    public DuplicateAccountException(String username) {
        super("Account " + username + " already exists");
    }
}