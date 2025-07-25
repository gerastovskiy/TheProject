package ru.services.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.core.commands.CreateBillingAccountCommand;
import ru.services.user.entity.UserEntity;
import ru.services.user.model.User;
import ru.services.user.model.UserRequest;
import ru.services.user.model.UserResponse;
import ru.services.user.validation.ValidateAfterMapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @ValidateAfterMapping
    @Mapping(target = "role", ignore = true)
    User toUser(UserRequest userRequest);
    @Mapping(target = "role", constant = "USER")
    UserEntity toUserEntity(User user);
    User toUser(UserEntity userEntity);
    UserResponse toResponse(User user);

    CreateBillingAccountCommand createBillingAccountCommand(User user);
}
