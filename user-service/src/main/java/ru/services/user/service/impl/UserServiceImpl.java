package ru.services.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.services.user.entity.UserEntity;
import ru.services.user.exception.DuplicateUserException;
import ru.services.user.exception.UserAlreadyCreated;
import ru.services.user.exception.UserNotFoundException;
import ru.services.user.mapper.UserMapper;
import ru.services.user.model.User;
import ru.services.user.repository.UserRepository;
import ru.services.user.service.MessageSenderService;
import ru.services.user.service.UserService;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final MessageSenderService messageSenderService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public User getUser(Long id) {
        UserEntity userEntity = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return mapper.toUser(userEntity);
    }

    @Override
    public User getUser(String username) {
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return mapper.toUser(userEntity);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException(id);

        userRepository.deleteById(id);
    }

    @Override
    public void deleteUser(String username) {
        if (!userRepository.existsByUsername(username))
            throw new UserNotFoundException(username);

        userRepository.deleteByUsername(username);
    }

    @Override
    public User createUser(User user, String idempotencyKey) {
        if (redisTemplate.hasKey(idempotencyKey))
            throw new UserAlreadyCreated(idempotencyKey);

        if (userRepository.existsByUsername(user.username()))
            throw new DuplicateUserException(user.username());

        UserEntity userEntity = mapper.toUserEntity(user);
        userRepository.save(userEntity);
        user = mapper.toUser(userEntity);
        redisTemplate.opsForValue().set(idempotencyKey, user.id().toString());
        messageSenderService.sendBillingMessage(mapper.createBillingAccountCommand(user));
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!userRepository.existsById(user.id()))
            throw new UserNotFoundException("User not found: id = " + user.id());

        UserEntity userEntity = mapper.toUserEntity(user);
        return mapper.toUser(userRepository.save(userEntity));
    }
}