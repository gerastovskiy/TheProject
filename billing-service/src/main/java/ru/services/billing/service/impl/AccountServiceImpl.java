package ru.services.billing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.services.billing.entity.AccountEntity;
import ru.services.billing.exception.DuplicateAccountException;
import ru.services.billing.exception.AccountNotFoundException;
import ru.services.billing.exception.InsufficientFundsException;
import ru.services.billing.mapper.AccountMapper;
import ru.services.billing.model.Account;
import ru.services.billing.repository.AccountRepository;
import ru.services.billing.service.AccountService;
import java.math.BigDecimal;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper mapper;

    @Override
    public Account createAccount(Account account) {
        if (accountRepository.existsByUsername(account.username()))
            throw new DuplicateAccountException(account.username());

        AccountEntity accountEntity = mapper.toAccountEntity(account);

        return mapper.toAccount(accountRepository.save(accountEntity));
    }

    private AccountEntity getAccountPrivate(String username) {
        AccountEntity accountEntity = accountRepository
                .findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException(username));

        return accountEntity;
    }

    @Override
    public Account getAccount(Long id) {
        AccountEntity accountEntity = accountRepository
                .findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        return mapper.toAccount(accountEntity);
    }

    @Override
    public Account getAccount(String username) {
        return mapper.toAccount(getAccountPrivate(username));
    }


    @Override
    public Account debitAccount(String username, BigDecimal amount) {
        AccountEntity accountEntity = getAccountPrivate(username);
        accountEntity.setAmount(accountEntity.getAmount().add(amount));
        accountRepository.save(accountEntity);

        return mapper.toAccount(accountEntity);
    }

    @Override
    public Account creditAccount(String username, BigDecimal amount) {
        AccountEntity accountEntity = getAccountPrivate(username);
        if (accountEntity.getAmount().compareTo(amount) < 0) throw new InsufficientFundsException(amount);

        accountEntity.setAmount(accountEntity.getAmount().subtract(amount));
        accountRepository.save(accountEntity);

        return mapper.toAccount(accountEntity);
    }
}