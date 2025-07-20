package ru.services.user.service;

import ru.services.user.model.User;

public interface UserService {
    User getUser(Long id);
    User getUser(String username);
    void deleteUser(Long id);
    void deleteUser(String username);
    User createUser(User user, String idempotencyKey);
    User updateUser(User user);
}