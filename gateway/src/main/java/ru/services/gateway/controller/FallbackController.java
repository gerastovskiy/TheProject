package ru.services.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/auth")
    public ResponseEntity<Map<String, Object>> authFallback() {
        return createFallbackResponse("Authentication service is unavailable");
    }

    @RequestMapping("/fallback/user")
    public ResponseEntity<Map<String, Object>> userFallback() {
        return createFallbackResponse("User service is unavailable");
    }

    @RequestMapping("/fallback/billing")
    public ResponseEntity<Map<String, Object>> billingFallback() {
        return createFallbackResponse("Billing service is unavailable");
    }

    @RequestMapping("/fallback/order")
    public ResponseEntity<Map<String, Object>> orderFallback() {
        return createFallbackResponse("Order service is unavailable");
    }

    @RequestMapping("/fallback/store")
    public ResponseEntity<Map<String, Object>> storeFallback() {
        return createFallbackResponse("Store service is unavailable");
    }

    private ResponseEntity<Map<String, Object>> createFallbackResponse(String message) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                        "error", HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                        "message", message,
                        "recoveryAction", "Please try again after 30 seconds"
                ));
    }
}
