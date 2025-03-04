package com.kitchenconnect.kitchen.repository;

import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.enums.KitchenStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenRepository extends JpaRepository<Kitchen, Long> {
    List<Kitchen> findTop3ByOrderByOverallRatingDesc();
    Kitchen findByUser(User user);
    List<Kitchen> findByStatus(KitchenStatus status);
}
