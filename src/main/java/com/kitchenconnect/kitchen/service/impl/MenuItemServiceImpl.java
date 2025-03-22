package com.kitchenconnect.kitchen.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kitchenconnect.kitchen.entity.FoodItem;
import com.kitchenconnect.kitchen.entity.MenuItem;
import com.kitchenconnect.kitchen.repository.MenuItemRepository;
import com.kitchenconnect.kitchen.service.MenuItemService;

import java.util.List;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItem> getMenuItemsByCategory(Long categoryId) {
        return menuItemRepository.findByCategoryId(categoryId);
    }

    public MenuItem createMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public MenuItem updateMenuItem(Long id, MenuItem updatedMenuItem) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu item not found"));
        menuItem.setName(updatedMenuItem.getName());
        menuItem.setDescription(updatedMenuItem.getDescription());
        menuItem.setPrice(updatedMenuItem.getPrice());
        menuItem.setIsVeg(updatedMenuItem.getIsVeg());
        menuItem.setImageUrl(updatedMenuItem.getImageUrl());
        return menuItemRepository.save(menuItem);
    }

    public boolean deleteMenuItem(Long id) {

        if (menuItemRepository.existsById(id)) {
            menuItemRepository.deleteById(id);
            return true; // Deletion successful
        }
        return false; // Entity does not exist  
    }

    public MenuItem getMenuItemById(Long id){
        return menuItemRepository.findById(id).orElse(null);
    }

    public List<MenuItem> getMenuItemsByIds(List<Long> ids){
        return menuItemRepository.findAllById(ids);
    }

    public List<MenuItem> getFeaturedMenuItems() {
        return menuItemRepository.findTop4ByOrderByOverallRatingDesc();
    }
}