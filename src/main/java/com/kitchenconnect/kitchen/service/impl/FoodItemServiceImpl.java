package com.kitchenconnect.kitchen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kitchenconnect.kitchen.entity.FoodItem;
import com.kitchenconnect.kitchen.repository.FoodItemRepository;
import com.kitchenconnect.kitchen.service.FoodItemService;


@Service
public class FoodItemServiceImpl implements FoodItemService {
    @Autowired
    private FoodItemRepository foodItemRepository;

   
    public List<FoodItem> getFeaturedFoodItems() {
        return foodItemRepository.findTop4ByOrderByOverallRatingDesc();
    }

    public List<FoodItem> findByKitchenId(Long id) {
        return foodItemRepository.findByKitchen_KitchenId(id);
    }

    public List<FoodItem> getFoodItemsByIds(List<Long> foodItemIds) {
        return foodItemRepository.findAllById(foodItemIds);
    }

    public FoodItem getFoodItemById(Long id){
        return foodItemRepository.findById(id).orElse(null);
    }
}


