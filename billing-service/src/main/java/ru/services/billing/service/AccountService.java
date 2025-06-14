package ru.services.billing.service;

import ru.services.billing.model.Account;
import java.math.BigDecimal;

public interface AccountService {
    Account createAccount(Account user);
    Account getAccount(Long id);
    Account getAccount(String username);
    Account debitAccount(String username, BigDecimal amount);
    Account creditAccount(String username, BigDecimal amount);
}