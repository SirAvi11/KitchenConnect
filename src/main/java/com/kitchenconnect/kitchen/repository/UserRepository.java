package com.kitchenconnect.kitchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kitchenconnect.kitchen.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);
}  
