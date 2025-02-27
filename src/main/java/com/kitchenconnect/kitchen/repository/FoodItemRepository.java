package com.kitchenconnect.kitchen.repository;

import com.kitchenconnect.kitchen.entity.FoodItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    List<FoodItem> findTop4ByOrderByOverallRatingDesc();
    List<FoodItem> findByKitchen_KitchenId(Long kitchenId);
}
