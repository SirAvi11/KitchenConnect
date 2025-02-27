package com.kitchenconnect.kitchen.service;

import java.util.List;
import java.util.Optional;

import com.kitchenconnect.kitchen.entity.User;

public interface UserService {
    List<User> getAllUsers();
    User registerUser (User user);
    Optional<User> findByUsernameOrEmail(String username, String email);
}
