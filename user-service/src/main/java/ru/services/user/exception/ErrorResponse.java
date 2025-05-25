package ru.services.user.exception;

import lombok.Getter;
import java.sql.Timestamp;
import java.util.Date;

@Getter
public class ErrorResponse {
    private final Timestamp timestamp = new Timestamp(new Date().getTime());
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}