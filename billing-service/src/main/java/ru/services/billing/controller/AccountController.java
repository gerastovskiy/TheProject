package ru.services.billing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.services.billing.mapper.AccountMapper;
import ru.services.billing.model.AccountResponse;
import ru.services.billing.service.AccountService;
import java.math.BigDecimal;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper mapper;

    @Operation(summary = "Debit account by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account debited"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PutMapping(value = "/debit{username}{amount}")
    public AccountResponse debitAccount(@RequestParam("username") String username, @RequestParam("amount") BigDecimal amount) {
        var account = accountService.debitAccount(username, amount);
        return mapper.toResponse(account);
    }

    @Operation(summary = "Credit account by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account credited"),
            @ApiResponse(responseCode = "404", description = "Account not found / Insufficient funds")
    })
    @PutMapping(value = "/credit{username}{amount}")
    public AccountResponse creditAccount(@RequestParam("username") String username, @RequestParam("amount") BigDecimal amount) {
        var account = accountService.creditAccount(username, amount);
        return mapper.toResponse(account);
    }
}