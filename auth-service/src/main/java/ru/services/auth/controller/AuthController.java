package ru.services.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.services.auth.exception.LoginException;
import ru.services.auth.model.Client;
import ru.services.auth.model.ErrorResponse;
import ru.services.auth.model.TokenResponse;
import ru.services.auth.service.ClientService;
import ru.services.auth.service.TokenService;
import ru.services.user.exception.RegistrationException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final ClientService clientService;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody Client client) {
        clientService.register(client.getClientId(), client.getClientSecret());
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/token")
    public TokenResponse getToken(@RequestBody Client client) {
        clientService.checkCredentials(client.getClientId(), client.getClientSecret());
        return new TokenResponse(tokenService.generateToken(client.getClientId()));
    }

    @ExceptionHandler({RegistrationException.class, LoginException.class})
    public ResponseEntity<ErrorResponse> handleUserRegistrationException(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }
}