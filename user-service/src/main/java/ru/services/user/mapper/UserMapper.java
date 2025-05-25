package ru.services.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.services.user.entity.UserEntity;
import ru.services.user.model.User;
import ru.services.user.model.UserRequest;
import ru.services.user.model.UserResponse;
import ru.services.user.validation.ValidateAfterMapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @ValidateAfterMapping
    User toUser(UserRequest userRequest);

    @Mapping(target = "role", constant = "USER")
    UserEntity toUserEntity(User user);

    User toUser(UserEntity userEntity);

    UserResponse toResponse(User user);
}
