package ru.services.auth.exception;

public class LoginException extends RuntimeException{
    public LoginException(String message) {
        super(message);
    }
}