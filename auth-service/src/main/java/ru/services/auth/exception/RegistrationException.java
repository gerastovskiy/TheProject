package ru.services.user.exception;

import lombok.Getter;
import java.sql.Timestamp;
import java.util.Date;

@Getter
public class RegistrationException extends RuntimeException {
    public RegistrationException(String message) {
        super(message);
    }
}