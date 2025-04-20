package com.kitchenconnect.kitchen.service;

import com.kitchenconnect.kitchen.entity.Chef;

public interface ChefService {
    Chef getChefById(Long id);
    Chef getChefByKitchenId(Long id);
    Chef updateChef(Chef chef);
    Chef getChefByUserId(Long userID); 
}

