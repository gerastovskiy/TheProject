package ru.services.billing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.core.commands.*;
import ru.core.events.*;
import ru.services.billing.entity.AccountEntity;
import ru.services.billing.model.Account;
import ru.services.billing.model.AccountRequest;
import ru.services.billing.model.AccountResponse;
import ru.services.billing.validation.ValidateAfterMapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @ValidateAfterMapping
    @Mapping(target = "amount", ignore = true)
    Account toAccount(AccountRequest Request);
    AccountEntity toAccountEntity(Account account);
    Account toAccount(AccountEntity accountEntity);
    AccountResponse toResponse(Account account);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "amount", constant = "0")
    })
    Account toAccount(CreateBillingAccountCommand command);

    @Mapping(target = "orderId", source = "orderId")
    PaymentProcessedEvent  toPaymentProcessedEvent(Account account, Long orderId);
    @Mappings({
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "orderId", source = "orderId"),
            @Mapping(target = "error", source = "error")
    })
    PaymentFailedEvent toPaymentFailedEvent(String username, Long orderId, String error);

    PaymentCalcelledEvent toPaymentCalcelledEvent(Account account, Long orderId);
}