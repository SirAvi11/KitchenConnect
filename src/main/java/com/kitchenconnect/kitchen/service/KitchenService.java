package com.kitchenconnect.kitchen.service;

import java.util.List;

import com.kitchenconnect.kitchen.entity.Kitchen;

public interface KitchenService {
    List<Kitchen> getAllKitchens();
    List<Kitchen> getFeaturedKitchens();
    Kitchen getKitchenById(Long id);
}
