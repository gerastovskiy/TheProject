package ru.services.auth.model;

import lombok.Data;

@Data
public class Client {
    String clientId;
    String clientSecret;
}
