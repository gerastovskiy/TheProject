package ru.services.billing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.services.billing.entity.AccountEntity;
import ru.services.billing.model.Account;
import ru.services.billing.model.AccountRequest;
import ru.services.billing.model.AccountResponse;
import ru.services.billing.model.UserResponse;
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
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "amount", constant = "0")
    })
    Account toAccount(UserResponse userResponse);
}
