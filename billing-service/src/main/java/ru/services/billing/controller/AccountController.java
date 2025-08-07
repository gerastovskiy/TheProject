package ru.services.billing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.services.billing.authorization.AccessControlService;
import ru.services.billing.mapper.AccountMapper;
import ru.services.billing.model.AccountResponse;
import ru.services.billing.service.AccountService;
import org.springframework.security.core.Authentication;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/billing/v1")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccessControlService accessControlService;
    private final AccountMapper mapper;

    @Operation(summary = "Debit account by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account debited"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PutMapping(value = "/debit{username}{amount}")
    @PreAuthorize("isAuthenticated()")
    public AccountResponse debitAccount(
            @RequestParam("username") String username,
            @RequestParam("amount") BigDecimal amount,
            @RequestHeader("X-Request-ID") String idempotencyKey,
            Authentication authentication) {

        String clientId = authentication.getName();

        if (!accessControlService.checkUsernameMatches(clientId, username)) {
            throw new AccessDeniedException("Access denied");
        }

        var account = accountService.debitAccount(username, amount, idempotencyKey);
        return mapper.toResponse(account);
    }

    @Operation(summary = "Credit account by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account credited"),
            @ApiResponse(responseCode = "404", description = "Account not found / Insufficient funds")
    })
    @PutMapping(value = "/credit{username}{amount}")
    @PreAuthorize("isAuthenticated()")
    public AccountResponse creditAccount(@RequestParam("username") String username,
                                         @RequestParam("amount") BigDecimal amount,
                                         @RequestHeader("X-Request-ID") String idempotencyKey,
                                         Authentication authentication) {

        String clientId = authentication.getName();

        if (!accessControlService.checkUsernameMatches(clientId, username)) {
            throw new AccessDeniedException("Access denied");
        }

        var account = accountService.creditAccount(username, amount, idempotencyKey);
        return mapper.toResponse(account);
    }
}