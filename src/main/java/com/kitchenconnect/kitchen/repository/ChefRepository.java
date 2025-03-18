package com.kitchenconnect.kitchen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kitchenconnect.kitchen.entity.Chef;

public interface ChefRepository extends JpaRepository<Chef, Long> {
    // Optional<Chef> findByUsername(String username);
    Optional<Chef> findByKitchen_KitchenId(Long id);
}  

