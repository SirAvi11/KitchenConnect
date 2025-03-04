package com.kitchenconnect.kitchen.service;

import java.util.List;
import java.util.Optional;

import com.kitchenconnect.kitchen.entity.User;

public interface UserService {
    List<User> getAllUsers();
    User saveUser (User user);
    Optional<User> getById (Long id);
    Optional<User> findByUsernameOrEmail(String username, String email);
}
