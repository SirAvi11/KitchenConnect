package com.kitchenconnect.kitchen.service;

import java.util.List;

import com.kitchenconnect.kitchen.entity.FoodItem;

public interface FoodItemService {
    List<FoodItem> getFeaturedFoodItems();
    List<FoodItem> findByKitchenId(Long id);
    List<FoodItem> getFoodItemsByIds(List<Long> ids);
}

