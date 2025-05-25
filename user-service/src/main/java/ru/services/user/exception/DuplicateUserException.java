package ru.services.user.exception;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException(String username) {
        super("User " + username + " already exists");
    }
}